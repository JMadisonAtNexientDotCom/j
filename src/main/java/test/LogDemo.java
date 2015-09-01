/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//SOURCE:
//http://examples.javacodegeeks.com/core-java/apache/commons/logging-commons/logfactory/org-apache-commons-logging-logfactory-example/
public class LogDemo
{
    private static Log logger = LogFactory.getLog(LogDemo.class);

    public static void doIt()
    {
	logger.info("Test info");
	logger.debug("Test info");

    }
}
