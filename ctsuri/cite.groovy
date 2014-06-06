// require(url='http://xml.apache.org/xalan-j/', jar='serializer.jar')
// require(url='http://xml.apache.org/xalan-j/', jar='xalan_270.jar')
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

//
// Groovlets can get direct access to URLs without sandboxing
// restrictions of in-browser ajax.

// Accepts two parameters:
// 		urn = a cts or cite urn
//		protocol = legit values:
//			cts, hmtcts, cite, citeimg


def reqUrn = request.getParameter("urn")
def protocol = request.getParameter("protocol")
def web = request.getParameter("web")
def svcType = ""
def svcUrl = ""
def fullUrl = ""
def svc = ""
def label = ""

// set up some variables for later
switch (protocol) {
	case 'servletcts':
		label = 'Furman University Canonical Text Services'
		svc = 'svc-folio-cts'
		svcType = 'cite-text'
		svcUrl = 'http://folio.furman.edu/citeservlet/texts'
		fullUrl = svcUrl + "?request=GetPassagePlus&urn="
		break
	case 'foliocts':
		label = 'Furman University Canonical Text Services'
		svc = 'svc-folio-cts'
		svcType = 'cite-text'
		svcUrl = 'http://folio.furman.edu/citeservlet/texts'
		fullUrl = svcUrl + "?request=GetPassagePlus&urn="
		break
	//case 'cts':
		//label = 'Furman University Canonical Text Services'
		//svc = 'svc-fu-cts'
		//svcType = 'cite-cts'
		//svcUrl = 'http://furman-folio.appspot.com/CTS?request=GetPassagePlus&urn='
		//break
	//case 'dmk':
		//label = 'DMK Canonical Text Services'
		//svc = 'svc-dmk-cite'
		//svcType = 'cite-coll'
		//svcUrl = 'http://cite-dmk.appspot.com/api?req=GetObject&urn='
		//break
	////case 'hmtcts':
		//label = 'Homer Multitext Canonical Text Services'
		//svc = 'svc-hmt-cts'
		//svcType = 'cite-cts'
		//svcUrl = 'http://hmt-cts.appspot.com/CTS?request=GetPassagePlus&urn='
		//break
	case 'cite':
		label = 'Furman University CITE Collection Service'
		svc = 'svc-fu-cite'
		svcType = 'cite-coll'
		svcUrl = 'http://folio.furman.edu/cfc/api?request=GetObjectPlus&urn='
		break
	//case 'citeimg':
		//label = 'Homer Multitext Image Services'
		//svc = 'svc-hmt-img'
		//svcType = 'cite-image'
		//svcUrl = 'http://amphoreus.hpcc.uh.edu/tomcat/chsimg/Img?&request=GetImagePlus&urn='
		//break

// Working
	case 'folioimg':
		label = 'Furman Folio Image Services'
		svc = 'svc-folio-img'
		svcType = 'cite-image'
		svcUrl = 'http://folio.furman.edu/citeservlet/images'
		break
	case 'hmtimg':
		label = 'Homer Multitext Image Services'
		svc = 'svc-newhmt-img'
		svcType = 'cite-image'
		svcUrl = 'http://beta.hpcc.uh.edu/tomcat/hmt/images?&request=GetImagePlus&urn='
		break
	default: 
		svc = 'svc-fu-cts'
		svcType = 'cite-cts'
		label = 'Furman University Canonical Text Services'
}


if ((protocol == "") || (protocol == null) || (reqUrn == "") || (reqUrn == null)) {
    // error
    if ((reqUrn == "") || (reqUrn == null)) {
		html.html {
			head { title "Error: no URN" }
			body {
			
				p "I could not get a URN out of this URI." 
				
				 }
		}
	}
	
	if ((protocol == "") || (protocol == null)) {
		html.html {
			head { title "Error: no protocol" }
			body {
			
				p "Missing a protocol request-parameter ('saws', 'cts', 'cite', or 'citeimg')." 
				
				 }
		}
	}
    
} else {
	if ((protocol == "servletcts")){
		if (web == "true"){
			html.html {
				head {
					title "${label}"
					meta(charset:'utf-8')				
					script(type:'text/javascript', src:'http://folio.furman.edu/citeservlet/citekit/js/cite-jq.js',' ')
					meta(name:'encoding', content:'UTF-8')
				}
				body {
					blockquote(class:"${svcType}  ${svc}",cite:"${reqUrn}"){ mkp.yield( "${reqUrn}" ) }
					ul(id:"citekit-sources") {
						li(class:"citekit-source ${svcType} citekit-default", id:"${svc}"){
							mkp.yield( "${svcUrl}")
						}
					}	
				}
			}
		} else {

			response.setContentType("text/xml; charset=UTF-8")
			def replyWriter = response.getWriter()
			StringBuffer remoteUrl = new StringBuffer(fullUrl)
			
			remoteUrl.append(reqUrn)
			
			def replyUrl = new URL (remoteUrl.toString())
			def replyXML = replyUrl.getText('UTF-8')
			
			replyWriter.print(replyUrl.getText('UTF-8'))
		}

	} else {
		if (web == 'true'){
			html.html {
				head {
					title "${label}"
					meta(charset:'utf-8')				
					script(type:'text/javascript', src:'http://folio.furman.edu/citeservlet/citekit/js/cite-jq.js',' ')
				}
				body {
					blockquote(class:"${svcType}  ${svc}",cite:"${reqUrn}"){ mkp.yield( "${reqUrn}" ) }
					ul(id:"citekit-sources") {
						li(class:"citekit-source ${svcType} citekit-default", id:"${svc}"){
							mkp.yield( "${svcUrl}")
						}
					}	
				
				}
			}
		} else {
			
			response.setContentType("text/xml; charset=UTF-8")
			def replyWriter = response.getWriter()
			
			StringBuffer remoteUrl = new StringBuffer(svcUrl)
			
			remoteUrl.append(reqUrn)
			
			def replyUrl = new URL (remoteUrl.toString())
			def replyXML = replyUrl.getText('UTF-8')
			
			replyWriter.print(replyUrl.getText('UTF-8'))
		}	
	}
}

