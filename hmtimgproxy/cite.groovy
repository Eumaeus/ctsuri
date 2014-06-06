import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

def svcUrl = 'http://beta.hpcc.uh.edu/tomcat/hmt/images?request=GetImagePlus&urn='
def reqUrn = request.getParameter("urn")

//		html.html {
//			head { title "Error: no protocol" }
//			body {
//			
//				p "Missing a protocol request-parameter ('saws', 'cts', 'cite', or 'citeimg')." 
//				
//				 }
//		}

response.setContentType("text/xml; charset=UTF-8")
def replyWriter = response.getWriter()
		
StringBuffer remoteUrl = new StringBuffer(svcUrl)
		
remoteUrl.append("${reqUrn}")
		
def replyUrl = new URL (remoteUrl.toString())
def replyXML = replyUrl.getText('UTF-8')
		
replyWriter.print(replyUrl.getText('UTF-8'))
