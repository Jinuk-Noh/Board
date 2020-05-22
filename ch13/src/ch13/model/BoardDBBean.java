package ch13.model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.naming.*;
import javax.sql.*;



public class BoardDBBean {
	private static BoardDBBean instance = new BoardDBBean();
	public static BoardDBBean getInstance() {
		return instance;
	}
	private BoardDBBean(){}
	
    private Connection getConnection() throws Exception {
		Context initCtx= new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/oracledb");
    	return ds.getConnection();
    }

	public void insertArticle(BoardDataBean article) throws Exception {
		Connection conn=null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int num = article.getNum();
		int ref = article.getRef();
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();
		int number = 0;
		String sql = "";
		
		try {
			conn = getConnection();
			if(conn==null)
				System.out.println("fail");
			else
				System.out.println("Connected");
			sql = "select max(num) from boarde";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				number = rs.getInt(1) + 1;
			} else {
				number = 1;
			}
			closeDBResources(rs, pstmt);			
			if(num!=0) {  // 댓글
				sql = "update boarde set re_step=re_step+1 where ref=? and re_step>?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, re_step);
				pstmt.executeUpdate();
				re_step = re_step+1;
				re_level = re_level+1;
			} else {  // 원본글쓰기 - 댓글x
				ref = number;
				re_step = 0;
				re_level = 0;				
			}
			closeDBResources(rs, pstmt);			
			//filename 추가
			sql="insert into boarde (num, writer, email, subject, passwd, reg_date, ";
			sql+=" ref, re_step, re_level, content, ip, filename) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, number);
            pstmt.setString(2, article.getWriter());
            pstmt.setString(3, article.getEmail());
            pstmt.setString(4, article.getSubject());
            pstmt.setString(5, article.getPasswd());
			pstmt.setTimestamp(6, article.getReg_date());
            pstmt.setInt(7, ref);
            pstmt.setInt(8, re_step);
            pstmt.setInt(9, re_level);
			pstmt.setString(10, article.getContent());
			pstmt.setString(11, article.getIp());
			pstmt.setString(12,article.getFileName());
			pstmt.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			closeDBResources(rs, pstmt, conn);
		}
	}
	
	//boarde테이블에 저장된 전체글의 수를 얻어냄(select문)<=list.jsp에서 사용
	public int getArticleCount() throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int x=0;
	    try {
	        conn = getConnection();
	        pstmt = conn.prepareStatement("select count(*) from boarde");
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	           x= rs.getInt(1);
			}
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    } finally {
	        closeDBResources(rs, pstmt, conn);
	    }
		return x;
	}
	
	public int getArticleCount(String searchType ,String search) throws Exception {
	    
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int x=0;
	    try {
	        conn = getConnection();
	        if(searchType.equals("num")) {
		        pstmt = conn.prepareStatement("select count(*) from boarde where num = ? or ref in (select ref from boarde where num = ?)");
		        pstmt.setInt(1, Integer.parseInt(search));
		        pstmt.setInt(2, Integer.parseInt(search));
		        }
	        else if(searchType.equals("writer")) {
		        pstmt = conn.prepareStatement("select count(*) from boarde where writer = ? or ref in (select ref from boarde where writer = ?)");
		        pstmt.setString(1, search);
		        pstmt.setString(2, search);
		        }
	        else if(searchType.equals("subject")) {
		        pstmt = conn.prepareStatement("select count(*) from boarde where subject = ? or ref in (select ref from boarde where subject = ?)");
		        pstmt.setString(1, search);
		        pstmt.setString(2, search);
		        }
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	           x= rs.getInt(1);
			}
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    } finally {
	        closeDBResources(rs, pstmt, conn);
	    }
		return x;
	}
	
	//글의 목록(복수개의 글)을 가져옴(select문) <=list.jsp에서 사용
	public List<BoardDataBean> getArticles(int start, int end)
	         throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<BoardDataBean> articleList=null;
	    try {
	        conn = getConnection();	        
	        // BoardDAO - boardList 메서드 - 페이징 처리
	        //String sql = "select * from boarde order by ref desc, re_step asc limit ?,? ";
	        String sql = "SELECT * "
	        		+ "FROM (SELECT ROWNUM rnum, B.* FROM "
	        			+ "	( SELECT * FROM BOARDE ORDER BY ref desc, re_step asc ) B ) ";
	        sql += " WHERE rnum >= ? and rnum <= ?";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, start);
			pstmt.setInt(2, end);
	        rs = pstmt.executeQuery();
		       	        
	        if (rs.next()) {
	            articleList = new ArrayList<BoardDataBean>(end);
	            do{
	              BoardDataBean article= new BoardDataBean();
				  article.setNum(rs.getInt("num"));
				  article.setWriter(rs.getString("writer"));
	              article.setEmail(rs.getString("email"));
	              article.setSubject(rs.getString("subject"));
	              article.setPasswd(rs.getString("passwd"));
			      article.setReg_date(rs.getTimestamp("reg_date"));
				  article.setReadcount(rs.getInt("readcount"));
	              article.setRef(rs.getInt("ref"));
	              article.setRe_step(rs.getInt("re_step"));
				  article.setRe_level(rs.getInt("re_level"));
	              article.setContent(rs.getString("content"));
			      article.setIp(rs.getString("ip")); 
			      // ejkim.a for file download
			      article.setFileName(rs.getString("filename"));
	              articleList.add(article);
			    }while(rs.next());
			}	              	       
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    } finally {
	    	closeDBResources(rs, pstmt, conn);
	    }
		return articleList;
	}
	
	public List<BoardDataBean> getArticles(int start, int end, String searchType, String search)
	         throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<BoardDataBean> articleList=null;
	    try {
	        conn = getConnection();	        
	        // BoardDAO - boardList 메서드 - 페이징 처리
	        //String sql = "select * from boarde order by ref desc, re_step asc limit ?,? ";
	        String sql = "";               
	        if(searchType.equals("writer")) {
	        	 sql = "SELECT * "
	 	        		+ "FROM (SELECT ROWNUM rnum, B.* FROM "
	 	        			+ "	( SELECT * FROM BOARDE ORDER BY ref desc, re_step asc ) B  "
	 	        			+ "where writer = ? or ref in (select ref from boarde where writer = ?))";
	        	 sql += " WHERE rnum >= ? and rnum <= ? ";
	 	        
	 	        pstmt = conn.prepareStatement(sql);
	 	        
	 			pstmt.setString(1, search);
	 			pstmt.setString(2, search);
	 			pstmt.setInt(3, start);
	 			pstmt.setInt(4, end);
	 			}	 
	        
	        
	        else if(searchType.equals("num")) {
	        	sql = "SELECT * "
	 	        		+ "FROM (SELECT ROWNUM rnum, B.* FROM "
	 	        			+ "	( SELECT * FROM BOARDE ORDER BY ref desc, re_step asc ) B"
	 	        			+ " where  num = ? or ref in (select ref from boarde where num = ?)) ";
	 	        sql += " WHERE rnum >= ? and rnum <= ?";
	 	        
	 	        pstmt = conn.prepareStatement(sql);	 	        
	 			pstmt.setInt(1, Integer.parseInt(search));
	 			pstmt.setInt(2, Integer.parseInt(search));
	 			pstmt.setInt(3, start);
	 			pstmt.setInt(4, end);
	 	     
	        }
	        	        
	        
	        else if(searchType.equals("subject")) {
	        	sql = "SELECT * "
	 	        		+ "FROM (SELECT ROWNUM rnum, B.* FROM "
	 	        			+ "	( SELECT * FROM BOARDE ORDER BY ref desc, re_step asc ) B "
	 	        			+ " where subject = ? or ref in (select ref from boarde where subject = ?)) ";
	        	sql += " WHERE rnum >= ? and rnum <= ? ";
	 	        
	 	        pstmt = conn.prepareStatement(sql);	 	        
	 			pstmt.setString(1, search);
	 			pstmt.setString(2, search);
	 			pstmt.setInt(3, start);
	 			pstmt.setInt(4, end);
	 	            	 	        
	        }
	        rs = pstmt.executeQuery(); 	   
	        
	        if (rs.next()) {
	            articleList = new ArrayList<BoardDataBean>(end);
	            do{
	              BoardDataBean article= new BoardDataBean();
				  article.setNum(rs.getInt("num"));
				  article.setWriter(rs.getString("writer"));
	              article.setEmail(rs.getString("email"));
	              article.setSubject(rs.getString("subject"));
	              article.setPasswd(rs.getString("passwd"));
			      article.setReg_date(rs.getTimestamp("reg_date"));
				  article.setReadcount(rs.getInt("readcount"));
	              article.setRef(rs.getInt("ref"));
	              article.setRe_step(rs.getInt("re_step"));
				  article.setRe_level(rs.getInt("re_level"));
	              article.setContent(rs.getString("content"));
			      article.setIp(rs.getString("ip")); 
			      // ejkim.a for file download
			      article.setFileName(rs.getString("filename"));
	              articleList.add(article);
			    }while(rs.next());
			}	              	       
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    } finally {
	    	closeDBResources(rs, pstmt, conn);
	    }
		return articleList;
	}
				
	
	// 글의 내용 보기
	public BoardDataBean getArticle(int num) throws Exception {
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDataBean article = null;
		
		try{
			conn =getConnection();
			
			pstmt =conn.prepareStatement("update boarde set readcount=readcount+1 where num =?");
			pstmt.setInt(1, num);
			pstmt.executeUpdate();									
            
			pstmt = conn.prepareStatement("select * from boarde where num = ?");
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                article = new BoardDataBean();
                article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setEmail(rs.getString("email"));
				article.setSubject(rs.getString("subject"));
				article.setPasswd(rs.getString("passwd"));
				article.setReg_date(rs.getTimestamp("reg_date"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setRe_step(rs.getInt("re_step"));
				article.setRe_level(rs.getInt("re_level"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));
				article.setFileName(rs.getString("filename"));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			closeDBResources(rs, pstmt, conn);			
		}
		return article;
	}
	
	//글수정 폼에서 사용할 글의 내용
	public BoardDataBean updateGetArticle(int num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDataBean article = null;
		try {
			conn =getConnection();
			
			pstmt = conn.prepareStatement("select * from boarde where num = ?");
			pstmt.setInt(1, num);
			rs =pstmt.executeQuery();
			
			if(rs.next()) {
				article = new BoardDataBean();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setEmail(rs.getString("email"));
				article.setSubject(rs.getString("subject"));
				article.setPasswd(rs.getString("passwd"));
				article.setReg_date(rs.getTimestamp("reg_date"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setRe_step(rs.getInt("readcount"));
				article.setRe_level(rs.getInt("re_level"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));
				article.setFileName(rs.getString("filename"));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			closeDBResources(rs, pstmt, conn);		
		}
		return article;
	}
	
	//글수정 처리
	public int updateArticle(BoardDataBean article) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		String dbpasswd="";
		String sql="";
		int x = -1;
		try {
			conn =getConnection();
			
			pstmt =conn.prepareStatement("select passwd from boarde where num =?");
			pstmt.setInt(1, article.getNum());
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd=rs.getString("passwd");
				if(dbpasswd.equals(article.getPasswd())) {
					sql="update boarde set writer =?, email=?, subject=?,passwd=?";
					sql+=",content=?, filename=?  where num=?";
					
					pstmt = conn.prepareStatement(sql);					
					pstmt.setString(1, article.getWriter());
					pstmt.setString(2, article.getEmail());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getPasswd());
					pstmt.setString(5, article.getContent());
					pstmt.setString(6, article.getFileName());
					pstmt.setInt(7, article.getNum());
					
					pstmt.executeUpdate();
					x=1;
				}else {
					x=0;
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			closeDBResources(rs, pstmt, conn);			
		}
		return x;
	}
	
	public int updateArticleNoFile(BoardDataBean article) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		String dbpasswd="";
		String sql="";
		int x = -1;
		try {
			conn =getConnection();
			
			pstmt =conn.prepareStatement("select passwd from boarde where num =?");
			pstmt.setInt(1, article.getNum());
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd=rs.getString("passwd");
				if(dbpasswd.equals(article.getPasswd())) {
					sql="update boarde set writer =?, email=?, subject=?,passwd=?";
					sql+=",content=?  where num=?";
					
					pstmt = conn.prepareStatement(sql);					
					pstmt.setString(1, article.getWriter());
					pstmt.setString(2, article.getEmail());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getPasswd());
					pstmt.setString(5, article.getContent());
					
					pstmt.setInt(6, article.getNum());
					
					pstmt.executeUpdate();
					x=1;
				}else {
					x=0;
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			closeDBResources(rs, pstmt, conn);				
		}
		return x;
	}
	
	//글삭제처리 사용
	public int deleteArticle(int num , String passwd) throws Exception{
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd="";
		int x = -1;
		try {
			conn=getConnection();
			
			pstmt=conn.prepareStatement("select passwd from boarde where num =?");
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("passwd");
				if(dbpasswd.equals(passwd)) {
					pstmt=conn.prepareStatement("delete from boarde where num =?");
					pstmt.setInt(1, num);
					pstmt.executeUpdate();
					x=1;
				}else
					x=0;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			closeDBResources(rs, pstmt, conn);				
		}
		return x;
	}
	
	public String checkIdPw(String id) throws Exception {
		String dbpasswd = null;
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;				
		try {
			conn=getConnection();
			
			pstmt=conn.prepareStatement("select passwd from member where id =?");
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("passwd");			
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			closeDBResources(rs, pstmt, conn);					
		}
		return dbpasswd;

	}
	
	public void insertJoin(MemberDataBean newMember) {
		Connection conn=null;
		PreparedStatement pstmt =null;				
		try {
				conn=getConnection();
				pstmt=conn.prepareStatement("insert into member values (?,?,?,?,?,?,?,?)");				
				
				pstmt.setString(1,newMember.getId());
				pstmt.setString(2,newMember.getPasswd());
				pstmt.setString(3,newMember.getDateNumber());
				pstmt.setString(4,newMember.getEmail());
				pstmt.setString(5,newMember.getAddress());
				pstmt.setString(6,newMember.getPhoneNumber());
				pstmt.setString(7,newMember.getName());
				pstmt.setTimestamp(8,newMember.getReg_date());
				pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			closeDBResources(pstmt, conn);
		}
	}
	
	private void closeDBResources(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void closeDBResources(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void closeDBResources(PreparedStatement pstmt, Connection conn) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void closeDBResources(Statement stmt, Connection conn) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void closeDBResources(ResultSet rs, PreparedStatement pstmt) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}


























