package webInterface;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;




/**
 * Servlet implementation class Searcher1
 */

public class Searcher1 extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Searcher1() {
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
		
		if(option.equals("Sigmod")) {
			List<String> result = new ArrayList<String>();
			List<String> forwardResult = new ArrayList<String>();
			float totalTime = 0;
			String temp = "";
			forwardResult.add(option);
			SigmodSearch sigmodSearch = new SigmodSearch();
			try {
				long startTime = System.currentTimeMillis();
				result = sigmodSearch.searchRank(name);
				long endTime   = System.currentTimeMillis();
				totalTime = (float) ((endTime - startTime) / 1000.0) ;
				System.out.println("Results Recieved: " + result.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0 ; i < result.size() ; i++) {
	        	//textArea.append(result.get(i));
	        	//append(result.get(i));
				System.out.println("Result from RankSearch: " + result.get(i));
	        	temp = temp + result.get(i);	
	        }
			forwardResult.add(Float.toString(totalTime));
			forwardResult.add(temp);
			request.setAttribute("result", forwardResult);
	        RequestDispatcher view = request.getRequestDispatcher("RankInterface.jsp");
	        view.forward(request, response);
			
		}
		else if(option.equals("Mircosoft")){
			
		}
		else {
			
		
		System.out.println("--------------->" + option);
		
		String temp = "";
		SearcherCode searcherCode = new SearcherCode();
		XMLSearcher XMLSearcher = new XMLSearcher();
		List<String> result = new ArrayList<String>();
		List<String> forwardResult = new ArrayList<String>();
		String w2vResult = "";
		try {
//			Word2VEC wordVec = new Word2VEC();
//			System.out.println(name.replaceAll(" ", "_"));
//			wordVec.SimilarWords(name.replaceAll(" ", "_"));
			ExePythonW2V exePython = new ExePythonW2V();
			String query = "py word2vec.py " + name.replaceAll(" ", "_");
		    w2vResult = exePython.exeW2V(query);
			System.out.println(w2vResult);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		//result = searcherCode.search(name);
//		result = searcherCode.search(name);
		System.out.println("Launching New Code");
		long startTime = System.currentTimeMillis();
		result = XMLSearcher.search(name,"all");
		long endTime   = System.currentTimeMillis();
		float totalTime = (float) ((endTime - startTime) / 1000.0) ;
//		try {
//			fakeresult = XMLSearcher.search(name);
//		}
//		catch(Exception e) {
//			System.out.println(e.getMessage());
//		}
		
		for(int i = 0 ; i < result.size() ; i++) {
        	//textArea.append(result.get(i));
        	//append(result.get(i));
        	temp = temp + result.get(i);	
        }
		forwardResult.add(Float.toString(totalTime));
		forwardResult.add(w2vResult);
		forwardResult.add(temp);
		request.setAttribute("result", forwardResult);
        RequestDispatcher view = request.getRequestDispatcher("RankInterface.jsp");
        view.forward(request, response);
		
//		response.setContentType("text/html");
//	    PrintWriter out = response.getWriter();
//	    
//	    out.println
//	      (
//	       "<head>"
//	    	+"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + 
//	    	"<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\r\n" + 
//	    	"<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n" + 
//	    	
//	        "<style>"
//	       + "B{"
//	       + "color: red;"
//	       + "}"
//	       
//	       + "</style>"
//	       + "</head>"
//	       + "<body>\n" +
//	       "<div class = \"w3-container\">"+
//	       			
//	    		   "<form action=\"searcher1\" method=\"post\">\r\n" +
//	    		   "<div class=\"w3-container  w3-display-topleft w3-pale-green\">"
//	    		   + 
//	    		   " <h3>Lucene Search <input type=\"text\"  name=\"search\" size = \"60\">"
//	    		   + "<button class=\"w3-button w3-xlarge w3-circle w3-teal\" type =\"submit\"><i class=\"fa fa-search\"></i></button></h3>\r\n" + 
//	    		   "</div>"
//	    		   + "<div class=\"w3-cell-row w3-padding-48\">"
//	    		   + "<div class = \"w3-cell w3-cell-top w3-padding-large \"> Search By: <br>\r\n" +
//	    		   "<input type=\"radio\" name=\"option\" value=\"all\" checked> All <br>\r\n" + 
//	    		   "  <input type=\"radio\" name=\"option\" value=\"authors\"> Authors <br>\r\n"
//	    		   + "<input type=\"radio\" name=\"option\" value=\"venue\"> Venue <br>\r\n" + 
//	    		   "  <input type=\"radio\" name=\"option\" value=\"year\"> Year "
////					"<a href = \"searcher1\">All</a><br>"+
////					"<a href = \"searcher1\">Authors</a><br>"+
////					"<a href = \"searcher1\">Venue</a><br>"+
////					"<a href = \"searcher1\">Years</a>"
//							
//	    		   + "</div>"
//	    		   +
//	    		   "</form>"+
//	    		   
//	    		   
//	       "<div class = \"w3-cell w3-cell-middle\">"+
//	       "<p>"
//	       + "Time Taken: " + totalTime + " seconds<br>" + temp +"</p>\n"
//	       		+ "</div></div>"
//	       		+ "</div>"
//	       		+ "<a href = \"index.jsp\">Back</a>" +
//	       "</body></html>");
		}
	}
}
