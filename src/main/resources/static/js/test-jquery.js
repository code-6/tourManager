if (typeof jQuery !== 'undefined') {
    alert("jQuery is loaded in a javascript file");
} else {
    alert("jQuery is not loaded in a javascript file");
}

$(function () {
    $('#daterange').daterangepicker({
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 5,
        autoApply: true,
        cancelLabel: 'Clear'
    }, function (start, end, label) {
        const pattern = "DD-MM-YYYY HH:mm";
        $('#daterange').val(start.format(pattern)+'-'+end.format(pattern));
    });
});

$(function() {
    $('#daterange').daterangepicker({
        timePicker: true,
        startDate: moment().startOf('hour'),
        endDate: moment().startOf('hour').add(32, 'hour'),
        locale: {
            format: 'M/DD hh:mm A'
        }
    });
});