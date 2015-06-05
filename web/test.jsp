<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link href="resource/stylesheet/charts.css" rel="stylesheet" type="text/css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.2/d3.min.js" charset="utf-8"></script>
    <script src="resource/js/nv.d3.js" charset="utf-8"></script>

    <style>
        text {
            font: 12px sans-serif;
        }
        svg {
            display: block;
        }
        html, body, #chart1, svg {
            margin: 0px;
            padding: 0px;
            height: 100%;
            width: 100%;
        }
    </style>
</head>
<body class='with-3d-shadow with-transitions'>

<div id="chart1"></div>

<script>
    // Wrapping in nv.addGraph allows for '0 timeout render', stores rendered charts in nv.graphs, and may do more in the future... it's NOT required
    var chart;

    nv.addGraph(function() {
        chart = nv.models.lineChart()
                .options({
                    transitionDuration: 300,
                    useInteractiveGuideline: true
                })
        ;

        // chart sub-models (ie. xAxis, yAxis, etc) when accessed directly, return themselves, not the parent chart, so need to chain separately
        chart.xAxis
                .axisLabel("Time (s)")
                .tickFormat(d3.format(',.1f'))
                .staggerLabels(true)
        ;

        chart.yAxis
                .axisLabel('Voltage (v)')
                .tickFormat(d3.format(',.2f'))
        ;

        d3.select('#chart1').append('svg')
                .datum(sinAndCos())
                .call(chart);

        nv.utils.windowResize(chart.update);

        return chart;
    });

    function sinAndCos() {
        var sin = [],
                cos = [],
                rand = [],
                rand2 = []
                ;

        for (var i = 0; i < 100; i++) {
            sin.push({x: i, y: i % 10 == 5 ? null : Math.sin(i/10) }); //the nulls are to show how defined works
            cos.push({x: i, y: .5 * Math.cos(i/10)});
            rand.push({x:i, y: Math.random() / 10});
            rand2.push({x: i, y: Math.cos(i/10) + Math.random() / 10 })
        }

        return [
            {
                area: true,
                values: sin,
                key: "Sine Wave",
                color: "#ff7f0e"
            },
            {
                values: cos,
                key: "Cosine Wave",
                color: "#2ca02c"
            },
            {
                values: rand,
                key: "Random Points",
                color: "#2222ff"
            }
            ,
            {
                values: rand2,
                key: "Random Cosine",
                color: "#667711"
            }
        ];
    }

</script>
</body>
</html>