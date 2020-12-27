<?php
include_once("/home/jayant/htdocs/book/ch04/vendor/autoload.php");

$config = array(
	"endpoint" => array(
		"localhost" => array(
			"host"=>"127.0.0.1",
			"port"=>"8983",
			"path"=>"/solr",
			"core"=>"collection1",
		),
	)
);

$client = new Solarium\Client($config);
//create a select query
$query = $client->createSelect();
//get the edismax component.
$query->setQuery('*:*');
//get the facetset component
$facetset = $query->getFacetSet();
$facetset->createFacetField('category')->setField('cat');
//return the top 5 facets only.
$facetset->setLimit(25);
//return all facets which have alteast 1 term in it.
$facetset->setMinCount(1);
//return all documents which do not have any value for the facet field
//$facetset->setMissing(true);
//fire the query and get the result
$resultSet = $client->select($query);
//number of results found
$found = $resultSet->getNumFound();
//echo "Results Found : $found<br/>".PHP_EOL;
//display facets for author
//echo PHP_EOL."Facets for cat:<br/>".PHP_EOL.PHP_EOL;
$facet_cat = $resultSet->getFacetSet()->getFacet('category');
/*foreach($facet_author as $item => $count)
{
	echo $item.": [".$count."] <br/>".PHP_EOL;
}*/


?>

<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Highcharts Example</title>

		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
		<style type="text/css">
${demo.css}
		</style>
		<script type="text/javascript">
$(function () {
    $('#container').highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Category and count'
        },
        subtitle: {
            text: 'Source: solr exampledocs'
        },
        xAxis: {
            categories: [<?php foreach($facet_cat as $item => $count) { echo "'".$item."',"; } ?> ],
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'count',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: ' numbers'
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: 'current',
                data: [<?php foreach($facet_cat as $item => $count) { echo $count.","; } ?>]
        }]
    });
});
		</script>
	</head>
	<body>
<script src="js/highcharts.js"></script>
<script src="js/modules/exporting.js"></script>

<div id="container" style="min-width: 310px; max-width: 700px; height: 350px; margin: 0 auto"></div>

	</body>
</html>
