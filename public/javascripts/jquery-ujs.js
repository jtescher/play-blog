(function($) {

    $('a[data-method]').click(function(event){
           var $this = $(this);
           event.preventDefault();

           if (window.confirm($this.data("confirm"))) {
               $.ajax({
                   url: $this.attr("href"),
                   type: $this.data("method")
               }).done(function(){
                   window.location.reload();
               });
           }
       });

})( jQuery );
