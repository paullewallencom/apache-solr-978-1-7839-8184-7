Copy the schema.xml and solr.xml to the <solr_installation>/example/solr/collection1/conf folder.

To index the csv files use the following command when the particular csv file is mentioned in the chapter.

java -Dtype=text/csv -jar <solr_path>/example/exampledocs/post.jar data_clothes.csv

Use the dictionary_schema.xml for the section "implementing semantic search" while creating the Solr core for dictionary index. Rename the file toe schema.xml and copy it in the specified location as mentioned in that section.