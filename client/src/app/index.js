$(function () {
  'use strict';

  $('#add-resource-modal').on('show.bs.modal', function() {
    // Initialize the jQuery File Upload widget:
    $('#fileupload').fileupload({
      // Uncomment the following to send cross-domain cookies:
      xhrFields: {withCredentials: true},
      url: '/resources/upload',
      dataType: 'json',
      progressall: function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#progress .bar').css(
          'width',
          progress + '%'
        );
      },
      done: function(e, data) {
        $.each(data.result.files, function(index, file) {
          $('<p>').text(file.name).appendTo(document.body);
        });
      }
    });
  });
});
