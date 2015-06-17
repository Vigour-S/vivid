'use strict';

window.Vivid = {
  cardTemplate: function(ele) {
    return '<div class="post-card" ' +
      'data-timestamp="' + ele.timestamp + '" data-url="' + ele.url + '">' +
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
  }
}