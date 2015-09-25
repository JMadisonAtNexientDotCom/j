
!!!!!!!! REFACTOR CAREFULLY! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   config.constants.ResourceRelativeFolderPaths references this folder.
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

This folder is for CSS and JS libraries that we want to inject
into our .JSP pages.

Currently at the time of writing this, we just have one master
file for CSS (CSSLibs.html) and one master file for (JSLibs.html)

The current implementation is a bit hackish because:
1. If library paths change in our project, CSSLibs.html and JSLibs.html
   will have bad paths in it.

2. All of the imports are relative imports. (Except for the hosted CDNS)
   And they expect the .JSP file to be in:
   <prjRoot>/src/main/webapp/nts

   Specifically at the root/base of nts folder.
   NOT within a sub-folder.

   If this is broken, pages will be broken.
   So for now, just conform to a flat file system.