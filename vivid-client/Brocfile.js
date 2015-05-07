var uglifyJavaScript = require('broccoli-uglify-js');
var compileES6 = require('broccoli-es6modules');
var compileSass = require('broccoli-sass');
var pickFiles = require('broccoli-funnel');
var mergeTrees = require('broccoli-merge-trees');
var findBowerTrees = require('broccoli-bower');
var env = require('broccoli-env').getEnv();

// create tree for files in the app folder
app = pickFiles('src', {
  srcDir: '/',
  destDir: 'vivid'
});

// create tree for files in the styles folder
styles = pickFiles('src', {
  srcDir: '/',
  destDir: 'vivid'
});

// create tree for files in the test folder
tests = pickFiles('src', {
  srcDir: '/',
  destDir: 'vivid/tests'
});

// create tree for vendor folder (no filters needed here)
var vendor = 'vendor';

// include app, styles and vendor trees
var sourceTrees = [app, styles, vendor];

// include tests if not in production
if (env !== 'production') {
  sourceTrees.push(tests);
}

// Add bower dependencies
// findBowerTrees uses heuristics to pick the lib directory and/or main files,
// and returns an array of trees for each bower package found.
sourceTrees = sourceTrees.concat(findBowerTrees());

// merge array into tree
var appAndDependencies = new mergeTrees(sourceTrees, { overwrite: true });

// Transpile ES6 modules and concatenate them,
// recursively including modules referenced by import statements.
var appJs = compileES6(appAndDependencies, {
  format: 'amd'
});

// compile sass
var appCss = compileSass(sourceTrees, 'vivid/app.scss', 'assets/app.css');

if (env === 'production') {
  // minify js
  appJs = uglifyJavaScript(appJs, {
    // mangle: false,
    // compress: false
  })
}

// create tree for public folder (no filters needed here)
var publicFiles = 'public';

// merge js, css and public file trees, and export them
module.exports = mergeTrees([appJs, appCss, publicFiles]);
