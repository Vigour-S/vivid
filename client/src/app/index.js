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
      }
    });
  });

  var $container = $('#timeline');

  $.ajax({
    url: '/timeline?last_updated_till=' + Date.now() + '&count=5',
    method: 'GET',
    dataType: 'json'
  }).done(function(data) {
    var cards = [];
    $.each(data.timeline, function(_, ele) {
      var datetime = ele.timestamp, $card = Vivid.cardTemplate(ele);
      cards.push($card);
      $('#timeline-next a').attr('href', '/timeline?last_updated_till=' + datetime + '&count=30');
    });
    $container.append(cards).imagesLoaded(function(){
      $container.masonry({
        // selector for entry content
        itemSelector: '.post-card',
        columnWidth: 250
      });
    });
    $container.infinitescroll({
      navSelector  : "#timeline-next",
      // selector for the NEXT link (to page 2)
      nextSelector : "#timeline-next a",
      // selector for all items you'll retrieve
      itemSelector : ".post-card",
      loading: {
        finishedMsg: 'No more to load.'
      },
      dataType: 'json',
      appendCallback: false
    }, function(json, opts) {
      var cards = [];
      $.each(json.timeline, function(_, ele) {
        var datetime = ele.timestamp, $card = Vivid.cardTemplate(ele);
        cards.push($card);
        $('#timeline-next a').attr('href', '/timeline?last_updated_till=' + datetime + '&count=30');
      });
      $container.append(cards).imagesLoaded(function() {
        $container.masonry('reloadItems');
        $container.masonry('layout');
      });
    });
  });

  $container.on('click', '.resource-preview', function() {
    window.location = $(this).closest('.post-card').data('url');
  });
});
