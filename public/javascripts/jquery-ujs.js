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

    $('form').submit(function(event){
        var $this = $(this);

        if ($this.find('input[name="_method"]').length) {
            event.preventDefault();
            console.log("SUBMITTING");
            $.ajax({
                url: $this.attr("action"),
                type: $this.find('input[name="_method"]').val(),
                data: $this.serialize(),
            }).done(function(data){
                console.log(data);
                window.location.reload();
            });
        }
    });

})( jQuery );
