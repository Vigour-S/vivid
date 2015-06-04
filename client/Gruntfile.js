module.exports = function(grunt) {
  grunt.initConfig({
    broccoli_build: {
      assets: {
        dest: 'build/',
        brocfile: 'Brocfile.js'
      }
    },
    clean: ['build']
  });

  grunt.loadNpmTasks('grunt-broccoli-build');
  grunt.loadNpmTasks('grunt-contrib-clean');

  grunt.registerTask('build', ['clean', 'broccoli_build']);

  grunt.registerTask('default', ['build']);
};
