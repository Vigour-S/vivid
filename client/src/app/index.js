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
        $('#progress .bar').css('width', progress + '%');
      }
    });
  });

  var $container = $('#timeline');

  $.ajax({
    url: '/timeline?last_updated_till=' + new Date().toISOString() + '&count=5',
    method: 'GET',
    dataType: 'json'
  }).done(function(data) {
    var cards = [];
    $.each(data.timeline, function(_, ele) {
      var datetime = new Date(ele.timestamp), $card = Vivid.cardTemplate(ele);
      cards.push($card);
      $container.data('last-updated', datetime.toISOString());
    });
    $container.append(cards).imagesLoaded(function(){
      $container.masonry({
        // selector for entry content
        itemSelector: '.post-card',
        columnWidth: 250
      });
    });
    $('#timeline-load-more').on('click', function() {
      $('#timeline-load-more').hide();
      $.ajax({
        url: '/timeline?last_updated_till=' + $container.data('last-updated') + '&count=5',
        method: 'GET',
        dataType: 'json'
      }).done(function(data) {
        var cards = [];
        if (data.timeline.length === 0) {
          $('#timeline').append('<p>Nothing more.</p>');
          $('#timeline-load-more').hide();
        } else {
          $.each(data.timeline, function (_, ele) {
            var datetime = new Date(ele.timestamp), $card = Vivid.cardTemplate(ele);
            cards.push($card);
            $container.data('last-updated', datetime.toISOString());
          });
          $container.append(cards).imagesLoaded(function () {
            $container.masonry('reloadItems');
            $container.masonry('layout');
          });
          $('#timeline-load-more').show();
        }
      });
    });
  });

  $container.on('click', '.resource-preview', function() {
    window.location = $(this).closest('.post-card').data('url');
  });
});
