# Cochrane-Library-Crawler

This design aims to provide a robust and scalable solution for extracting metadata from the Cochrane Library review collection. I chose to implement a Java command-line application for its versatility and ease of deployment. By leveraging the Jsoup library for HTML parsing and Apache HttpClient for making HTTP requests, we can ensure efficient data extraction.
The application follows a straightforward flow to gather and store metadata:
Prompt the user to input a topic of interest.
Search for the topic on the Cochrane Library website and retrieve the list of relevant reviews.
Iterate through the search results, extracting metadata such as URL, title, author, and date for each review.
Write the gathered metadata to a text file in the specified format.