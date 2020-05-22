package ch13.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import ch13.model.BoardDBBean;
import ch13.model.BoardDataBean;
import ch13.model.MemberDataBean;

/**
 * Servlet implementation class BoardServlet
 */
@WebServlet("*.do")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BoardServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// PrintWriter out = response.getWriter();
		// OutputStream out =response.getOutputStream();
		String uri = request.getRequestURI();
		// System.out.println("################### nju uri: " + uri); <<확인용 문자출력
		int lastIndex = uri.lastIndexOf("/");
		String action = uri.substring(lastIndex + 1);
		// System.out.println("################### nju action: " + action); <<확인용 문자출력
		String viewPage = null;
		if (action.equals("write.do")) {
			viewPage = "writeForm.jsp";
		}
		if (action.equals("writePro.do")) {
			// ejkim.a for file upload // ejkim.test
			String realFolder = "";// 웹 어플리케이션상의 절대 경로
			String filename = "";

			// 파일이 업로드되는 폴더를 지정한다.
			String saveFolder = "/fileSave";
			String encType = "utf-8"; // 엔코딩타입
			int maxSize = 5 * 1024 * 1024; // 최대 업로될 파일크기 5Mb

			ServletContext context = getServletContext();
			// 현재 jsp페이지의 웹 어플리케이션상의 절대 경로를 구한다
			realFolder = context.getRealPath(saveFolder);
			MultipartRequest multi = null;
			
			BoardDataBean art = new BoardDataBean();
			try {
				// 전송을 담당할 콤포넌트를 생성하고 파일을 전송한다.
				// 전송할 파일명을 가지고 있는 객체, 서버상의 절대경로,최대 업로드될 파일크기, 문자코드, 기본 보안 적용
				multi = new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());

				// Form의 파라미터 목록을 가져온다
				// Enumeration<?> params = multi.getParameterNames();

				// 전송한 파일 정보를 가져와 출력한다
				Enumeration<?> files = multi.getFileNames();
				// 파일 정보가 있다면
				while (files.hasMoreElements()) {
					// input 태그의 속성이 file인 태그의 name 속성값 :파라미터이름
					String name = (String) files.nextElement();
					// 서버에 저장된 파일 이름
					filename = multi.getFilesystemName(name);
				}

				
				art.setNum(Integer.parseInt(multi.getParameter("num")));
				art.setWriter(multi.getParameter("writer"));
				art.setSubject(multi.getParameter("subject"));
				art.setEmail(multi.getParameter("email"));
				art.setContent(multi.getParameter("content"));
				art.setPasswd(multi.getParameter("passwd"));
				art.setRef(Integer.parseInt(multi.getParameter("ref")));
				art.setRe_step(Integer.parseInt(multi.getParameter("re_step")));
				art.setRe_level(Integer.parseInt(multi.getParameter("re_level")));

				art.setReg_date(new Timestamp(System.currentTimeMillis()));
				art.setIp(request.getRemoteAddr());
				// ejkim.a for file upload
				art.setFileName(filename);

				BoardDBBean dbPro = BoardDBBean.getInstance();
				dbPro.insertArticle(art);

			} catch (IOException ioe) {
				System.out.println(ioe);
			} catch (Exception ex) {
				System.out.println(ex);
			}
			request.setAttribute("num", art.getNum());
			request.setAttribute("re_step", art.getRe_step());
			request.setAttribute("re_level", art.getRe_level());
			request.setAttribute("subject", art.getSubject());
			request.setAttribute("content", art.getContent());
			request.setAttribute("passwd", art.getPasswd());
			
			viewPage = "writePro.jsp";
		}
		
		
		if (action.equals("updatePro.do")) {
			int check=0;
			String pageNum="";
			// ejkim.a for file upload
			String realFolder = "";// 웹 어플리케이션상의 절대 경로
			String filename = "";
			String saveFolder = "/fileSave";
			String encType = "utf-8"; // 엔코딩타입
			int maxSize = 5 * 1024 * 1024; // 최대 업로될 파일크기 5Mb
			ServletContext context = getServletContext();
			realFolder = context.getRealPath(saveFolder);
			MultipartRequest multi = null;		
					
			
			try {
				multi = new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());
				Enumeration<?> files = multi.getFileNames();
				while (files.hasMoreElements()) {
					String name = (String) files.nextElement();
					filename = multi.getFilesystemName(name);
				}						
				BoardDataBean art = new BoardDataBean();	
				art.setNum(Integer.parseInt(multi.getParameter("num")));
				art.setWriter(multi.getParameter("writer"));				
				art.setSubject(multi.getParameter("subject"));
				art.setEmail(multi.getParameter("email"));
				art.setContent(multi.getParameter("content"));
				art.setPasswd(multi.getParameter("passwd"));
//				art.setRef(Integer.parseInt(multi.getParameter("ref")));
//				art.setRe_step(Integer.parseInt(multi.getParameter("re_step")));
//				art.setRe_level(Integer.parseInt(multi.getParameter("re_level")));
//				art.setReg_date(new Timestamp(System.currentTimeMillis()));
//				art.setIp(request.getRemoteAddr());
				// ejkim.a for file upload
				art.setFileName(filename);
				pageNum=multi.getParameter("pageNum");
				
				BoardDBBean dbPro = BoardDBBean.getInstance();
				if(art.getFileName()==null) {
					check=dbPro.updateArticleNoFile(art);
				}
				else {
					check=dbPro.updateArticle(art);
				}
			} catch (IOException ioe) {
				System.out.println(ioe);
			} catch (Exception ex) {
				System.out.println(ex);
			}			
			
			request.setAttribute("check", check);
			request.setAttribute("pageNum", pageNum);
			
			viewPage = "updatePro.jsp";
		}
		if (action.equals("list.do")) {
			
			viewPage = "list.jsp";
		}
		if (action.equals("content.do")) {
		   int num = Integer.parseInt(request.getParameter("num"));
		   String pageNum = request.getParameter("pageNum");
		   try{
		      BoardDBBean dbPro = BoardDBBean.getInstance(); 
		      BoardDataBean article =  dbPro.getArticle(num);
			  request.setAttribute("vo", article);
		   	} catch(Exception e) {
		   		e.printStackTrace();
		   	}
		   request.setAttribute("num", num);
		   request.setAttribute("pageNum", pageNum);
			viewPage = "content.jsp";
		}
	
		if (action.equals("deletePro.do")) {
			int check = 0;
			int num = Integer.parseInt(request.getParameter("num"));			
			String pageNum = request.getParameter("pageNum");
			String passwd = request.getParameter("passwd");
			
			try {
				BoardDBBean dbPro = BoardDBBean.getInstance();
				check = dbPro.deleteArticle(num, passwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("check", check);
			viewPage = "deletePro.jsp";
		}
		
		if(action.contentEquals("loginPro.do")) {
			String id = request.getParameter("id");
			request.setAttribute("loginId", id);
		
			
			String passwd = request.getParameter("passwd");
			// PrintWriter out  = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			try {
				BoardDBBean dbPro = BoardDBBean.getInstance();
				String pwd = dbPro.checkIdPw(id);
				if (pwd == null) {
					// id를 찾을 수 없음
					request.setAttribute("checkId", -1);
					viewPage = "loginPro.jsp";
				} else if (passwd.equals(pwd)) {
					// id, passwd 동일, 로그인 성공
					request.setAttribute("checkId", 0);
					request.getSession().setAttribute("loginId", id);
					viewPage = "loginPro.jsp";
				} else {
					// passwd 가 틀림
					request.setAttribute("checkId", 1);
					viewPage = "loginPro.jsp";
//					out.print("<script type=text/javascript>");
//					out.print("alert('아이디와 패스워드가 일치하지 않습니다.');");
//					out.print("history.back();");
//					out.print("</script>");					
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equals("joinPro.do")) {
			MemberDataBean newMember = new MemberDataBean();
			newMember.setId(request.getParameter("id"));
			newMember.setPasswd(request.getParameter("passwd"));			
			String checkPasswd = request.getParameter("checkPasswd");
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String day = request.getParameter("day");
			String dateNumber = year+month+day;
			newMember.setDateNumber(dateNumber);
			
			String email1 = request.getParameter("email1");
			String email2 = request.getParameter("email2");
			String email =email1+"@"+email2;
			newMember.setEmail(email);
			
			newMember.setAddress(request.getParameter("address"));
			newMember.setPhoneNumber(request.getParameter("phoneNumber"));
			newMember.setName(request.getParameter("name"));
			newMember.setReg_date(new Timestamp(System.currentTimeMillis()));
			
			response.setContentType("text/html;charset=UTF-8");
			
			
			try {
			BoardDBBean dbPro = BoardDBBean.getInstance();
			String dbPasswd = dbPro.checkIdPw(newMember.getId());
			
			if (dbPasswd != null) {
				// 이미 존재하는 ID 가입 실패
				request.setAttribute("check", 1);
				viewPage = "joinPro.jsp";					
			} else {
				if(newMember.getId().equals("")||newMember.getName().equals("")||
						newMember.getPasswd().equals("")) {
					//이름, 아이디, 비밀번호 미입력 가입 실패
					request.setAttribute("check", -2);
					viewPage = "joinPro.jsp";			
				}else if(!newMember.getPasswd().equals(checkPasswd)) {
					// 확인비밀번호 불일치 가입 실패					
					request.setAttribute("check", -1);
					viewPage = "joinPro.jsp";										
				}else {
					// id가 없음&& 입력 비밀번호 일치 회원가입 성공
					request.setAttribute("check", 0);
					dbPro.insertJoin(newMember);
					viewPage = "joinPro.jsp";
				}									
			}			
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	
		RequestDispatcher rDis = request.getRequestDispatcher(viewPage);
		rDis.forward(request, response);
	}
	
	

}
