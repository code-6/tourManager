$(document).ready(function(){
  $(".search-field").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $(".grid tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});