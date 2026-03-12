document.addEventListener('DOMContentLoaded', function () {
    var connections = [
        // frontend -> nginx
        ['frontend', 'nginx'],

        // nginx -> services
        ['nginx', 'oauth2-proxy'],
        ['nginx', 'backend-service'],
        ['nginx', 'user-service'],

        // oauth2-proxy -> keycloak
        ['oauth2-proxy', 'keycloak'],

        // backend-service connections
        ['backend-service', 'postgres'],
        ['backend-service', 'minio'],
        ['backend-service', 'profanity'],

        // user-service connections
        ['user-service', 'keycloak'],
        ['user-service', 'postgres'],
        ['user-service', 'profanity'],

        // monitoring
        ['prometheus', 'backend-service'],
        ['prometheus', 'cadvisor'],
        ['grafana', 'prometheus'],
    ];

    var svg = document.getElementById('connections');

    function getCenter(el) {
        var rect = el.getBoundingClientRect();
        var parentRect = el.parentElement.getBoundingClientRect();
        return {
            x: rect.left - parentRect.left + rect.width / 2,
            y: rect.top - parentRect.top + rect.height / 2,
        };
    }

    function drawLines() {
        svg.innerHTML = '';

        connections.forEach(function (pair) {
            var a = document.getElementById(pair[0]);
            var b = document.getElementById(pair[1]);
            if (!a || !b) return;

            var from = getCenter(a);
            var to = getCenter(b);

            var line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
            line.setAttribute('x1', from.x);
            line.setAttribute('y1', from.y);
            line.setAttribute('x2', to.x);
            line.setAttribute('y2', to.y);
            svg.appendChild(line);
        });
    }

    drawLines();
    window.addEventListener('resize', drawLines);
});
