/* Copyright (c) 2004-2005 The Dojo Foundation, Licensed under the Academic Free License version 2.1 or above */if(typeof djConfig=="undefined"){var djConfig={};}var dojo;if(typeof dojo=="undefined"){dojo={};}var dj_global=this;function dj_debug(){var _1=arguments;if(typeof dojo.hostenv.println!="function"){dj_throw("attempt to call dj_debug when there is no dojo.hostenv println implementation (yet?)");}if(!dojo.hostenv.is_debug_){return;}var _2=dj_global["jum"];var s=_2?"":"DEBUG: ";for(var i=0;i<_1.length;++i){s+=_1[i];}if(_2){jum.debug(s);}else{dojo.hostenv.println(s);}}function dj_throw(_5){if((typeof dojo.hostenv!="undefined")&&(typeof dojo.hostenv.println!="undefined")){dojo.hostenv.println("fatal error: "+_5);}throw Error(_5);}function dj_error_to_string(_6){return (typeof _6.message!=="undefined"?_6.message:(typeof _6.description!=="undefined"?_6.description:_6));}function dj_rethrow(_5,_6){var _7=dj_error_to_string(_6);dj_throw(_5+": "+_7);}function dj_eval(s){return eval(s);}function dj_unimplemented(_9,_10){var _11="No implementation of function '"+_9+"'";if((typeof _10!="undefined")&&(_10)){_11+=" "+_10;}_11+=" (host environment '"+dojo.hostenv.getName()+"')";dj_throw(_11);}function dj_inherits(_12,_13){if(typeof _13!="function"){dj_throw("eek: superclass not a function: "+_13+"\nsubclass is: "+_12);}_12.prototype=new _13();_12.prototype.constructor=_12;_12["super"]=_13;}dojo.render={name:"",ver:0,os:{win:false,linux:false,osx:false},html:{capable:false,support:{builtin:false,plugin:false},ie:false,opera:false,khtml:false,safari:false,moz:false},svg:{capable:false,support:{builtin:false,plugin:false},corel:false,adobe:false,batik:false},swf:{capable:false,support:{builtin:false,plugin:false},mm:false},swt:{capable:false,support:{builtin:false,plugin:false},ibm:false}};dojo.hostenv={is_debug_:((typeof djConfig["isDebug"]=="undefined")?false:djConfig["isDebug"]),base_script_uri_:((typeof djConfig["baseScriptUri"]=="undefined")?undefined:djConfig["baseScriptUri"]),base_relative_path_:((typeof djConfig["baseRelativePath"]=="undefined")?"":djConfig["baseRelativePath"]),library_script_uri_:((typeof djConfig["libraryScriptUri"]=="undefined")?"":djConfig["libraryScriptUri"]),auto_build_widgets_:((typeof djConfig["parseWidgets"]=="undefined")?true:djConfig["parseWidgets"]),loading_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modules_:{}};dojo.hostenv.name_="(unset)";dojo.hostenv.version_="(unset)";dojo.hostenv.pkgFileName="__package__";dojo.hostenv.getName=function(){return this.name_;};dojo.hostenv.getVersion=function(){return this.version_;};dojo.hostenv.getText=function(uri){dj_unimplemented("dojo.hostenv.getText","uri="+uri);};dojo.hostenv.getLibraryScriptUri=function(){dj_unimplemented("dojo.hostenv.getLibraryScriptUri","");};dojo.hostenv.getBaseScriptUri=function(){if(typeof this.base_script_uri_!="undefined"){return this.base_script_uri_;}var uri=this.library_script_uri_;if(!uri){uri=this.library_script_uri_=this.getLibraryScriptUri();if(!uri){dj_throw("Nothing returned by getLibraryScriptUri(): "+uri);}}var _15=uri.lastIndexOf("/");this.base_script_uri_=this.base_relative_path_;return this.base_script_uri_;};dojo.hostenv.normPath=function(_16){_16=_16.replace(/(\/\/)(\/)+/,"/");_16=_16.replace(/(\.\.)(\.)+/,"..");_16=_16.replace(/^(\.)+(\/)/,"");if(_16.indexOf("..")>=0){var _17=_16.split("/");var _18=[];for(var x=0;x<_17.length;x++){if(_17[x]==".."){if(_18.length){_18.pop();}else{_18.push("..");}}else{_18.push(_17[x]);}}return _18.join("/");}};dojo.hostenv.setBaseScriptUri=function(uri){this.base_script_uri_=uri;};dojo.hostenv.loadPath=function(_21,_22,cb){if(!_21){dj_throw("Missing relpath argument");}if((_21.charAt(0)=="/")||(_21.match(/^\w+:/))){dj_throw("Illegal argument '"+_21+"'; must be relative path");}var _24=this.getBaseScriptUri();var uri=_24+_21;try{var ok;if(!_22){ok=this.loadUri(uri);}else{ok=this.loadUriAndCheck(uri,_22);}return ok;}catch(e){return false;}};dojo.hostenv.loadUri=function(uri,cb){if(dojo.hostenv.loadedUris[uri]){return;}var _27=this.getText(uri,null,true);if(_27==null){return 0;}var _28=dj_eval(_27);return 1;};dojo.hostenv.getDepsForEval=function(_29){if(!_29){_29="";}var _30=[];var tmp=_29.match(/dojo.hostenv.loadModule\(.*?\)/mg);if(tmp){for(var x=0;x<tmp.length;x++){_30.push(tmp[x]);}}tmp=_29.match(/dojo.hostenv.require\(.*?\)/mg);if(tmp){for(var x=0;x<tmp.length;x++){_30.push(tmp[x]);}}tmp=_29.match(/dojo.hostenv.conditionalLoadModule\([\w\W]*?\)/gm);if(tmp){for(var x=0;x<tmp.length;x++){_30.push(tmp[x]);}}return _30;};dojo.hostenv.getTextStack=[];dojo.hostenv.loadUriStack=[];dojo.hostenv.loadedUris=[];dojo.hostenv.loadUriAndCheck=function(uri,_22,cb){var ok=true;try{ok=this.loadUri(uri,cb);}catch(e){dj_debug("failed loading ",uri," with error: ",e);}return ((ok)&&(this.findModule(_22,false)))?true:false;};dojo.hostenv.modulesLoadedFired=false;dojo.hostenv.modulesLoadedListeners=[];dojo.hostenv.modulesLoaded=function(){if(this.modulesLoadedFired){return;}if((this.loadUriStack.length==0)&&(this.getTextStack.length==0)){if(this.inFlightCount>0){dj_debug("couldn't initialize, there are files still in flight");return;}this.modulesLoadedFired=true;var mll=this.modulesLoadedListeners;for(var x=0;x<mll.length;x++){mll[x]();}}};dojo.hostenv.loadModule=function(_36,_37,_38){var _22=this.findModule(_36,0);if(_22){return _22;}if(typeof this.loading_modules_[_36]!=="undefined"){dj_debug("recursive attempt to load module '"+_36+"'");}else{this.addedToLoadingCount.push(_36);}this.loading_modules_[_36]=1;var _21=_36.replace(/\./g,"/")+".js";var _39=_36.split(".");var _40=_36.split(".");if(_39[0]=="dojo"){_39[0]="src";}var _41=_39.pop();_39.push(_41);if(_41=="*"){_36=(_40.slice(0,-1)).join(".");var _22=this.findModule(_36,0);if(_22){return _22;}while(_39.length){_39.pop();_39.push("__package__");_21=_39.join("/")+".js";if(_21.charAt(0)=="/"){_21=_21.slice(1);}ok=this.loadPath(_21,((!_38)?_36:null));if(ok){break;}_39.pop();}}else{_21=_39.join("/")+".js";_36=_40.join(".");var ok=this.loadPath(_21,((!_38)?_36:null));if((!ok)&&(!_37)){_39.pop();while(_39.length){_21=_39.join("/")+".js";ok=this.loadPath(_21,((!_38)?_36:null));if(ok){break;}_39.pop();_21=_39.join("/")+"/__package__.js";if(_21.charAt(0)=="/"){_21=_21.slice(1);}ok=this.loadPath(_21,((!_38)?_36:null));if(ok){break;}}}if((!ok)&&(!_38)){dj_throw("Could not find module '"+_36+"'; last tried path '"+_21+"'");}}if(!_38){_22=this.findModule(_36,false);if(!_22){dj_throw("Module symbol '"+_36+"' is not defined after loading '"+_21+"'");}}return _22;};function dj_load(_36,_37){return dojo.hostenv.loadModule(_36,_37);}function dj_eval_object_path(_43){if(typeof _43!="string"){return dj_global;}if(_43.indexOf(".")==-1){dj_debug("typeof this[",_43,"]=",typeof (this[_43])," and typeof dj_global[]=",typeof (dj_global[_43]));return (typeof dj_global[_43]=="undefined")?undefined:dj_global[_43];}var _44=_43.split(/\./);var obj=dj_global;for(var i=0;i<_44.length;++i){obj=obj[_44[i]];if((typeof obj=="undefined")||(!obj)){return obj;}}return obj;}dojo.hostenv.startPackage=function(_47){var _48=_47.split(/\./);if(_48[_48.length-1]=="*"){_48.pop();dj_debug("startPackage: popped a *, new packagename is : ",sysm.join("."));}var obj=dj_global;var _50="dj_global";for(var i=0;i<_48.length;++i){var _52=obj[_48[i]];_50+="."+_48[i];if((eval("typeof "+_50+" == 'undefined'"))||(eval("!"+_50))){dj_debug("startPackage: defining: ",_48.slice(0,i+1).join("."));obj=dj_global;for(var x=0;x<i;x++){obj=obj[_48[x]];}obj[_48[i]]={};}}return obj;};dojo.hostenv.findModule=function(_36,_54){if(typeof this.modules_[_36]!="undefined"){return this.modules_[_36];}var _55=dj_eval_object_path(_36);if((typeof _55!=="undefined")&&(_55)){return this.modules_[_36]=_55;}if(_54){dj_throw("no loaded module named '"+_36+"'");}return null;};if(typeof window=="undefined"){dj_throw("no window object");}with(dojo.render){html.UA=navigator.userAgent;html.AV=navigator.appVersion;html.capable=true;html.support.builtin=true;ver=parseFloat(html.AV);os.mac=html.AV.indexOf("Macintosh")==-1?false:true;os.win=html.AV.indexOf("Windows")==-1?false:true;html.opera=html.UA.indexOf("Opera")==-1?false:true;html.khtml=((html.AV.indexOf("Konqueror")>=0)||(html.AV.indexOf("Safari")>=0))?true:false;html.safari=(html.AV.indexOf("Safari")>=0)?true:false;html.moz=((html.UA.indexOf("Gecko")>=0)&&(!html.khtml))?true:false;html.ie=((document.all)&&(!html.opera))?true:false;html.ie50=html.ie&&html.AV.indexOf("MSIE 5.0")>=0;html.ie55=html.ie&&html.AV.indexOf("MSIE 5.5")>=0;html.ie60=html.ie&&html.AV.indexOf("MSIE 6.0")>=0;}dojo.hostenv.startPackage("dojo.hostenv");dojo.hostenv.name_="browser";var DJ_XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];dojo.hostenv.getXmlhttpObject=function(){var _56=null;var _57=null;try{_56=new XMLHttpRequest();}catch(e){}if(!_56){for(var i=0;i<3;++i){var _59=DJ_XMLHTTP_PROGIDS[i];try{_56=new ActiveXObject(_59);}catch(e){_57=e;}if(_56){DJ_XMLHTTP_PROGIDS=[_59];dj_debug("successfully made an ActiveXObject using progid ",_59);break;}else{}}}if((_57)&&(!_56)){dj_rethrow("Could not create a new ActiveXObject using any of the progids "+DJ_XMLHTTP_PROGIDS.join(", "),_57);}else{if(!_56){return dj_throw("No XMLHTTP implementation available, for uri "+uri);}}return _56;};dojo.hostenv.getText=function(uri,_60,_61){var _62=this.getXmlhttpObject();if(_60){_62.onreadystatechange=function(){if((4==_62.readyState)&&(_62["status"])){if(_62.status==200){dj_debug("LOADED URI: "+uri);_60(_62.responseText);}}};}_62.open("GET",uri,_60?true:false);_62.send(null);if(_60){return null;}return _62.responseText;};function dj_last_script_src(){var _63=window.document.getElementsByTagName("script");if(_63.length<1){dj_throw("No script elements in window.document, so can't figure out my script src");}var _64=_63[_63.length-1];var src=_64.src;if(!src){dj_throw("Last script element (out of "+_63.length+") has no src");}return src;}if(!dojo.hostenv["library_script_uri_"]){dojo.hostenv.library_script_uri_=dj_last_script_src();}dojo.hostenv.println=function(s){try{var ti=document.createElement("div");document.body.appendChild(ti);ti.innerHTML=s;}catch(e){}};window.onload=function(evt){dojo.hostenv.modulesLoaded();};dojo.hostenv.modulesLoadedListeners.push(function(){if(dojo.hostenv.auto_build_widgets_){if(dj_eval_object_path("dojo.webui.widgets.Parse")){try{var _68=new dojo.xml.Parse();var _69=_68.parseElement(document.body,null,true);var _70=new dojo.webui.widgets.Parse(_69);_70.createComponents(_69);}catch(e){dj_debug(e);}}}});document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.base_relative_path_+"/blank.html")+"'></iframe>");dojo.hostenv.conditionalLoadModule=function(_71){var _72=_71["common"]||[];var _73=(_71[dojo.hostenv.name_])?_72.concat(_71[dojo.hostenv.name_]||[]):_72.concat(_71["default"]||[]);for(var x=0;x<_73.length;x++){var _75=_73[x];if(_75.constructor==Array){dojo.hostenv.loadModule.apply(dojo.hostenv,_75);}else{dojo.hostenv.loadModule(_75);}}};dojo.hostenv.require=dojo.hostenv.loadModule;dojo.hostenv.provide=dojo.hostenv.startPackage;dj_debug("Using host environment: ",dojo.hostenv.name_);dj_debug("getBaseScriptUri()=",dojo.hostenv.getBaseScriptUri());dojo.hostenv.startPackage("dojo.io.IO");dojo.io.transports=[];dojo.io.hdlrFuncNames=["load","error"];dojo.io.Event=function(_76,_77){this.type=_76||"unknown";this.data=_77;};dojo.io.Error=function(msg,_76,num){this.message=msg;this.type=_76||"unknown";this.number=num||0;};dojo.io.transports.addTransport=function(_80){this.push(_80);this[_80]=dojo.io[_80];};dojo.io.bind=function(_81){if(!_81["mimetype"]){_81.mimetype="text/plain";}if(!_81["method"]){_81.method="get";}if(!_81["handle"]){_81.handle=function(){};}for(var x=0;x<this.hdlrFuncNames.length;x++){var fn=this.hdlrFuncNames[x];if(typeof _81[fn]=="function"){continue;}if(typeof _81.handler=="object"){if(typeof _81.handler[fn]=="function"){_81[fn]=_81.handler[fn]||_81.handler["handle"]||function(){};}}else{if(typeof _81["handler"]=="function"){_81[fn]=_81.handler;}else{if(typeof _81["handle"]=="function"){_81[fn]=_81.handle;}}}}var _84="";if(_81["transport"]){_84=_81["transport"];if(!this[_84]){return false;}}else{for(var x=0;x<dojo.io.transports.length;x++){var tmp=dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(_81))){_84=tmp;}}if(_84==""){return false;}}this[_84].bind(_81);return true;};dojo.io.argsFromMap=function(map){var _87=new Object();var _88="";for(var x in map){if(!_87[x]){_88+=encodeURIComponent(x)+"="+encodeURIComponent(map[x])+";";}}return _88;};dojo.hostenv.startPackage("dojo.io.BrowserIO");dojo.hostenv.loadModule("dojo.io.IO");dojo.io.checkChildrenForFile=function(_90){var _91=false;for(var x=0;x<_90.childNodes.length;x++){if(_90.nodeType==1){if(_90.nodeName.toLowerCase()=="input"){if(_90.getAttribute("type")=="file"){return true;}}if(_90.childNodes.length){if(checkChildrenForFile(_90)){return true;}}}}return false;};dojo.io.formHasFile=function(_93){return dojo.io.checkChildrenForFile(_93);};dojo.io.buildFormGetString=function(_94){var ec=encodeURIComponent;var _96="";var _97=_94.nodeName?_94.nodeName.toLowerCase():"";var _98=_94.type?_94.type.toLowerCase():"";if(((_97=="input")&&(_98!="radio")&&(_98!="checkbox"))||(_97=="select")||(_97=="textarea")){if(!((_97=="select")&&(_94.getAttribute("multiple")))){_96=ec(_94.getAttribute("name"))+"="+ec(_94.value)+"&";}else{var tn=ec(_94.getAttribute("name"));var _100=_94.getElementsByTagName("option");for(var x=0;x<_100.length;x++){if(_100[x].hasAttribute("selected")){_96+=tn+"="+ec(_100[x].value)+"&";}}}}else{if(_97=="input"){if(_94.checked){_96=ec(_94.getAttribute("name"))+"="+ec(_94.value)+"&";}}}if(_94.hasChildNodes()){for(var _102=(_94.childNodes.length-1);_102>=0;_102=_102-1){_96+=dojo.io.buildFormGetString(_94.childNodes.item(_102));}}return _96;};dojo.io.setIFrameSrc=function(_103,src,_105){try{var r=dojo.render.html;if(!_105){if(r.safari){_103.location=src;}else{frames[_103.name].location=src;}}else{var idoc=(r.moz)?_103.contentWindow:_103;idoc.location.replace(src);dj_debug(_103.contentWindow.location);}}catch(e){alert(e);}};dojo.io.createIFrame=function(_108){if(window[_108]){return window[_108];}if(window.frames[_108]){return window.frames[_108];}var r=dojo.render.html;var _110=null;_110=document.createElement((((r.ie)&&(r.win))?"<iframe name="+_108+">":"iframe"));with(_110){_80=_108;setAttribute("name",_108);id=_108;}window[_108]=_110;document.body.appendChild(_110);with(_110.style){position="absolute";left=top="0px";height=width="1px";visibility="hidden";if(dojo.hostenv.is_debug_){position="relative";height="100px";width="300px";visibility="visible";}}dojo.io.setIFrameSrc(_110,dojo.hostenv.base_relative_path_+"/blank.html",true);return _110;};dojo.io.cancelDOMEvent=function(evt){if(!evt){return false;}if(evt.preventDefault){evt.stopPropagation();evt.preventDefault();}else{if(window.event){window.event.cancelBubble=true;window.event.returnValue=false;}}return false;};dojo.io.XMLHTTPTransport=new function(){this.initialHref=window.location.href;this.initialHash=window.location.hash;this.moveForward=false;this.historyStack=[];this.forwardStack=[];this.historyIframe=null;this.bookmarkAnchor=null;this.locationTimer=null;this.addToHistory=function(args){var _112=args["back"]||args["backButton"]||args["handle"];var hash=null;if(!this.historyIframe){this.historyIframe=window.frames["djhistory"];}if(!this.bookmarkAnchor){this.bookmarkAnchor=document.createElement("a");document.body.appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display="none";}if((!args["changeURL"])||(dojo.render.html.ie)){var url=dojo.hostenv.base_relative_path_+"blank.html?"+(new Date()).getTime();this.moveForward=true;dojo.io.setIFrameSrc(this.historyIframe,url,false);}if(args["changeURL"]){hash="#"+((args["changeURL"]!==true)?args["changeURL"]:(new Date()).getTime());setTimeout("window.location.href = '"+hash+"';",1);this.bookmarkAnchor.href=hash;if(dojo.render.html.ie){var _115=_112;var lh=null;var hsl=this.historyStack.length-1;if(hsl>=0){while(!this.historyStack[hsl]["urlHash"]){hsl--;}lh=this.historyStack[hsl]["urlHash"];}if(lh){_112=function(){if(window.location.hash!=""){setTimeout("window.location.href = '"+lh+"';",1);}_115();};}this.forwardStack=[];var _118=args["forward"]||args["forwardbutton"];var tfw=function(){if(window.location.hash!=""){window.location.href=hash;}if(_118){_118();}};if(args["forward"]){args.forward=tfw;}else{if(args["forwardButton"]){args.forwardButton=tfw;}}}else{if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer=setInterval("dojo.io.XMLHTTPTransport.checkLocation();",200);}}}}this.historyStack.push({"url":url,"callback":_112,"kwArgs":args,"urlHash":hash});};this.checkLocation=function(){var hsl=this.historyStack.length;if((window.location.hash==this.initialHash)||(window.location.href==this.initialHref)&&(hsl==1)){this.handleBackButton();return;}if(this.forwardStack.length>0){if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){this.handleForwardButton();return;}}if((hsl>=2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}};this.iframeLoaded=function(evt,_120){var isp=_120.href.split("?");if(isp.length<2){if(this.historyStack.length==1){this.handleBackButton();}return;}var _122=isp[1];if(this.moveForward){this.moveForward=false;return;}var last=this.historyStack.pop();if(!last){if(this.forwardStack.length>0){var next=this.forwardStack[this.forwardStack.length-1];if(_122==next.url.split("?")[1]){this.handleForwardButton();}}return;}this.historyStack.push(last);if(this.historyStack.length>=2){if(isp==this.historyStack[this.historyStack.length-2].url){this.handleBackButton();}}else{this.handleBackButton();}};this.handleBackButton=function(){var last=this.historyStack.pop();if(!last){return;}if(last["callback"]){last.callback();}else{if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else{if(last.kwArgs["back"]){last.kwArgs["back"]();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}}}this.forwardStack.push(last);};this.handleForwardButton=function(){var last=this.forwardStack.pop();if(!last){return;}if(last.kwArgs["forward"]){last.kwArgs.back();}else{if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}}}this.historyStack.push(last);};this.canHandle=function(_81){if(((_81["mimetype"]=="text/plain")||(_81["mimetype"]=="text/html")||(_81["mimetype"]=="text/xml")||(_81["mimetype"]=="text/javascript"))&&((_81["method"]=="get")||((_81["method"]=="post")&&((!_81["formNode"])||(dojo.io.formHasFile(_81["formNode"])))))){return true;}return false;};this.bind=function(_81){if(!_81["url"]){if(((!_81["formNode"]))&&((_81["backButton"])||(_81["back"])||(_81["changeURL"])||(_81["watchForURL"]))){this.addToHistory(_81);return true;}}var _62=dojo.hostenv.getXmlhttpObject();var _127=false;_62.onreadystatechange=function(){if((4==_62.readyState)&&(_62["status"])){if(_127){return;}_127=true;if(_62.status==200){var ret=_62.responseText;if(_81.mimetype=="text/javascript"){ret=dj_eval(_62.responseText);}else{if(_81.mimetype=="text/xml"){ret=_62.responseXML;}}_81.load("load",ret,_62);}else{var _129=new dojo.io.Error("sampleTransport Error: "+_62.status+" "+_62.statusText);_81.error("error",_129);}}};var url=_81.url;var _130="";if(_81["formNode"]){if(_81.formNode.getAttribute("action")){url=_81.formNode.getAttribute("action");}_130=dojo.io.buildFormGetString(_81.formNode);}if(_81["content"]){_130=dojo.io.argsFromMap(_81.content);}if(_81["postContent"]){_130=_81.postContent;}if((_81["backButton"])||(_81["back"])||(_81["changeURL"])){this.addToHistory(_81);}if(_81.method=="post"){_62.open("POST",url,true);_62.send(_130);}else{_62.open("GET",url+"?"+_130,true);_62.send(null);}return;};dojo.io.transports.addTransport("XMLHTTPTransport");};