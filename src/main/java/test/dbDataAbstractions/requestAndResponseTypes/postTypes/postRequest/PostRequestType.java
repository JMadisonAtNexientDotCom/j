package test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest;

import test.dbDataAbstractions.requestAndResponseTypes.RequestType;

/**
 * The base class of objects meant to be used in an HTTP-POST REQUEST.
 * AKA: The [UI/Client] is uploading this object to the [server/back-end].
 * 
 * NOTE: This does not mean the server cannot send PostRequestType objects down
 * to the UI. In fact. Sending down EMPTY PostRequestTypes from server so that
 * the UI can fill them out seems, at the moment, to be a pretty optimal 
 * solution to keeping the front-end and back-end well integrated.
 * 
 * Rather than beam down a schema to the client. Beam down an empty instance
 * of the object. Then let the UI fill out the instance and send it back.
 * 
 * It's basically like... Sending out a blank form, filling it out. And
 * then sending it back. Like when you get a driver's license renewal form
 * in the mail.... Which I did not. And now I have a 275 dollar fine to pay
 * for driving with an expired license.
 * 
 * @author jmadison :2015.10.04
 */
public class PostRequestType extends RequestType{
    
    //Nothing in body. Just used for type saftey.
    
}//CLASS::END
