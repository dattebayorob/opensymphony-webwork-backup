<%-- 
	nonFieldValidatorsExample.jsp
	
	@author tm_jee
	@version $Date$ $Id$
--%>


<%@taglib prefix="ww" uri="/webwork" %>

<html>
	<head>
		<title>Showcase - Validation - Non Field Validator Example</title>
		<ww:url id="siteCss" value="/validation/validationExamplesStyles.css" includeContext="true" />
		<ww:head />
		<!-- link rel="stylesheet" type="text/css" href='<ww:property value="%{siteCss}" />'-->
	</head>
	<body>
	
	   
	   <!-- START SNIPPET: nonFieldValidatorsExample -->
		<ww:actionerror />
	
		<ww:form method="POST" action="submitNonFieldValidatorsExamples" namespace="/validation">
			<ww:textfield name="someText" label="Some Text" />
			<ww:textfield name="someTextRetype" label="Retype Some Text" />  
			<ww:textfield name="someTextRetypeAgain" label="Retype Some Text Again" />
			<ww:submit label="Submit" />
		</ww:form>
		
		
		<!--  END SNIPPET: nonFieldValidatorsExample -->
		
		
		<ww:include value="footer.jsp" />
	</body>
</html>

