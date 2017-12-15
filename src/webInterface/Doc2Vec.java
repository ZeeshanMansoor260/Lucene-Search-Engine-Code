package webInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Doc2Vec
 */
//@WebServlet("/Doc2Vec")
public class Doc2Vec extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Doc2Vec() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String name = request.getParameter("search");
		String option = request.getParameter("option");
		System.out.println(name);
		String d2vResult = "";
		List<String> forwardResult = new ArrayList<String>();
		try {
//			Word2VEC wordVec = new Word2VEC();
//			System.out.println(name.replaceAll(" ", "_"));
//			wordVec.SimilarWords(name.replaceAll(" ", "_"));
			ExePythonW2V exePython = new ExePythonW2V();
			String query = "py doc2vec.py " + name.replaceAll(" ", "_");
		    d2vResult = exePython.exeW2V(query);
		    d2vResult = d2vResult.replaceAll(":", "<br>");
			System.out.println("Result from Server" + d2vResult);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		forwardResult.add(d2vResult);
		request.setAttribute("result", forwardResult);
        RequestDispatcher view = request.getRequestDispatcher("doc2vec.jsp");
        view.forward(request, response);
	}

}
