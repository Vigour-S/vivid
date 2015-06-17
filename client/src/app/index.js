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

  var $container = $('#timeline');

  $.ajax({
    url: '/timeline?last_updated_till=' + new Date().toISOString() + '&count=30',
    method: 'GET',
    dataType: 'json'
  }).done(function(data) {
    var cards = []
    $.each(data.timeline, function(_, ele) {
      var $card = '<div class="post-card" data-url="' + ele.url + '">' +
        '<div class="resource-preview">' +
        '<img src="' + ele.description + '">' +
        '</div>' +
        '<div class="user-info">' +
        '<div class="user-avatar">' +
        '<a href="' + '/users/' + ele.username + '">' +
        '<img src="' + ele.avatar + '">' +
        '</a>' +
        '</div>' +
        '<span class="user-name">' +
        '<a href="' + '/users/' + ele.username + '">' +
        ele.username + '</a></span>' +
        '</div>' +
      '</div>';
      cards.push($card);
    });
    $container.append(cards).imagesLoaded(function(){
      $container.masonry({
        // selector for entry content
        itemSelector: '.post-card',
        columnWidth: 250
      });
    });
  });

  $container.infinitescroll({
    navSelector  : ".navigation",
    // selector for the NEXT link (to page 2)
    nextSelector : ".nav-previous a",
    // selector for all items you'll retrieve
    itemSelector : ".post-card",
    loading: {
      finishedMsg: 'No more pages to load.'
    }
  }, function(newElements) {
    var $newElems = $(newElements);
    $container.imagesLoaded(function() {
      $container.masonry('appended', $newElems);
    });
  });

  $container.on('click', '.resource-preview', function() {
    window.location = $(this).closest('.post-card').data('url');
  });
});
