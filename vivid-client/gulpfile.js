var gulpFilter = require('gulp-filter'),
    cram = require('gulp-cram'),
    uglify = require('gulp-uglify'),
    bowerSrc = require('gulp-bower-src'),
    sourcemaps = require('gulp-sourcemaps'),
    cssmin = require('gulp-minify-css'),
    gulp = require('gulp');

var paths = {
    run: 'src/run.js',
    css: {
        files: ['src/css/*.css'],
        root: 'src/css'
    },
    assets: ['src/img*/**','src/*.txt','src/*.html','src/font*/**','src/css*/filterable-list.css'],
    dest: './dist/'
};


gulp.task('build', function(){ });