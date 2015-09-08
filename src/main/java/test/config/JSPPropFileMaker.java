package test.config;

/**
 * Handles population of .properties file to be used by .JSP pages.
 * Specifically: Populates .properties file with API mappings so that
 * editing references within .JSP pages is not a nightmare. 
 * 
 * As I think out the logic of this... 
 * 1. It would be a lot of hard work + complexity to read from .properties file
 *    and use the values read from .properties file to create a valid api URL
 *    for an api call.
 * 
 * 2. This dynamic mapping to API calls:
 *    1. Adds confusion because now we have DOUBLED our variables that reference
 *       the same api.
 *    2. Though it makes refactoring "easy" because the .jsp pages will not BREAK
 *       after a refactor. After re-factor, the code may make less sense to read,
 *       though it may not be broken.
 * 
 *       Example: Say we have an api AT:  <host>/peopleAPI/getPeople
 *       And say our .jsp page is something like:
 *       peopleAPIURL = .propertiesFile.peopleAPIURL;
 * 
 *       Later when we refactor <host>/peopleAPI/getPeople
 *       to  <host>/studentAPI/getStudents...
 * 
 *       The .properties file will partially refactor:
 *       ///////////////  .properties file: /////////////
 *       peopleAIPURL = <host>/peopleAPI/getPeople
 *       ////////////////////////////////////////////////
 * 
 *       The .jsp page will still have the same code of:
 *       peopleAPIURL = .propertiesFile.peopleAPIURL;
 *       The code will STILL WORK... But the readability and 
 *       self-documenting aspect of the code will be broken.
 * 
 *       So, before... I thought this was a good solution to make
 *       the .jsp pages refactor safe... But now I do not think so.
 *       And I do not know a good solution....
 * 
 *       Going to keep it simple and stick with .html pages using
 *       angularJS and the RESTful apis I have created.
 * @author jmadison
 */
public class JSPPropFileMaker {
    //DON'T USE THIS CLASS.
    //Kept documentation in the header as to why
    //I have decided this is a bad decision.
}
