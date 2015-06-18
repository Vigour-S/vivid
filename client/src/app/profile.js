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

  var $container = $('#timeline'), regex = /users\/([a-z]+)/ig, username = regex.exec(window.location.href)[1];

  $.ajax({
    url: '/timeline2?last_updated_till=' + new Date().toISOString() + '&count=5&username=' + username,
    method: 'GET',
    dataType: 'json'
  }).done(function(data) {
    var cards = [];
    $.each(data.timeline, function(_, ele) {
      var datetime = new Date(ele.timestamp), $card = Vivid.cardTemplate(ele),
          lastUpdated = $container.data('last-updated');
      cards.push($card);
      if (lastUpdated > datetime.toISOString())
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
        url: '/timeline2?last_updated_till=' + $container.data('last-updated') + '&count=5&username=' + username,
        method: 'GET',
        dataType: 'json'
      }).done(function(data) {
        var cards = [];
        if (data.timeline.length === 0) {
          $('#timeline').append('<p>Nothing more.</p>');
          $('#timeline-load-more').hide();
        } else {
          $.each(data.timeline, function (_, ele) {
            var datetime = new Date(ele.timestamp), $card = Vivid.cardTemplate(ele),
                lastUpdated = $container.data('last-updated');
            cards.push($card);
            if (lastUpdated > datetime.toISOString())
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

});
