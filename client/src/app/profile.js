$(function () {
  'use strict';

  // Initialize the jQuery File Upload widget:
  $('#fileupload').fileupload({
    // Uncomment the following to send cross-domain cookies:
    xhrFields: {withCredentials: true},
    url: '/avatar',
    dataType: 'json',
    progressall: function (e, data) {
      $('#fileupload').closest('div').append('<span>Upload successfully.</span>');
    },
    done: function(e, data) {
      $.each(data.result.files, function(index, file) {
        $('<p>').text(file.name).appendTo(document.body);
      });
    }
  });

});
