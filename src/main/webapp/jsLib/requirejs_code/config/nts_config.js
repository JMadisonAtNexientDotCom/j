
//JMADISON NOTE:2015.10.26:
//
//Use this config file for html and jsp pages in "nts" folder.
//
//The relative folder paths need to be configured as if this file lives
//with the HTML file calling it. When this config file is imported via a
//script file, it is NOT aware of where it has been imported from.
//
//This fact gave me a huge headache when trying to figure out how to 
//Effectively use config.js files in the require.js framework.


// For any third party dependencies, like jQuery, place them in the lib folder.

// Configure loading modules from the lib directory,
// except for 'app' ones, which are in a sibling
// directory.
requirejs.config({
    baseUrl: '../jsLib/requirejs_code/madison_lib',
		
		//paths not needed:
    paths: {
       app_rel_path: '../madison_app'
    }
});

// Start loading the main app file. Put all of
// your application logic in there.
requirejs(['app_rel_path/NexFracLoader']);
