$(document).ready(function () {
    $('td').on("dragstart", function (event) {
        event.originalEvent.dataTransfer.setData("from", event.currentTarget.id);
    });

    $('td').on("dragover", function (event) {
        event.preventDefault();
    }).on("drop", function (event) {
        event.preventDefault();
        let from = event.originalEvent.dataTransfer.getData("from");
        let to = event.currentTarget.id;

        var form = document.createElement('form');
        form.setAttribute('method', 'post');
        form.setAttribute('action', $('.textBoxWrapper').attr('value'));

        var sourceField = document.createElement('input');
        sourceField.setAttribute('name', 'source');
        sourceField.setAttribute('type', 'hidden');
        sourceField.value = from;

        var targetField = document.createElement('input');
        targetField.setAttribute('name', 'target');
        targetField.setAttribute('type', 'hidden');
        targetField.value = to;

        form.appendChild(sourceField);
        form.appendChild(targetField);

        document.body.appendChild(form);
        form.submit();
    });
});
