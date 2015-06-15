var path = require('path'),
    cssMinify = require('gulp-minify-css'),
    sass = require('gulp-sass'),
    gulp = require('gulp');

var paths = {
    baseUrl: 'file:' + process.cwd() + '/src/',
    bowerLibs: ['src/lib/**', '!src/lib/*/test/*'],
    css: {
        files: ['src/css/*.css']
    },
    js: 'src/app/*',
    sass: ['src/sass/*'],
    assets: ["src/cache.manifest"],
    images: ["src/img/*"],
    public: ['public/*', 'src/lib/font-awesome-sass/assets/fonts/font-awesome/*'],
    rootAssets: ['src/config.js', 'src/robots.txt'],
    destination: './dist'
};

// Optimize application CSS files and copy to "dist" folder
gulp.task('optimize-and-copy-css', function() {
    return gulp.src(paths.css.files)
        .pipe(cssMinify())
        .pipe(gulp.dest(paths.destination + '/css'));
});

// Optimize application JavaScript files and copy to "dist" folder
gulp.task('optimize-and-copy-js', function(cb) {
    return gulp.src(paths.js)
        .pipe(gulp.dest(paths.destination + '/app'));
});

// Copy jspm-managed JavaScript dependencies to "dist" folder
gulp.task('copy-lib', function() {
    return gulp.src(paths.bowerLibs)
        .pipe(gulp.dest(paths.destination + '/lib'));
});

gulp.task('copy-images', function() {
    return gulp.src(paths.images)
        .pipe(gulp.dest(paths.destination + '/img'));
});

gulp.task('copy-assets', function() {
    return gulp.src(paths.assets)
        .pipe(gulp.dest(paths.destination))
});

gulp.task('sass', function () {
    return gulp.src(paths.sass)
        .pipe(sass())
        .pipe(cssMinify({noRebase: true}))
        .pipe(gulp.dest(paths.destination + '/css'));
});

gulp.task('copy-public', function () {
    return gulp.src(paths.public)
        .pipe(gulp.dest(paths.destination + '/public'));
});
gulp.task('copy-root-assets', function () {
    return gulp.src(paths.rootAssets)
        .pipe(gulp.dest(paths.destination));
});


gulp.task('build', ['optimize-and-copy-css', 'optimize-and-copy-js', 'copy-lib',
    'copy-images', 'sass', 'copy-assets', 'copy-public', 'copy-root-assets'], function(){});