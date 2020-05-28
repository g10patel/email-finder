import edu.depaul.email.*;
import org.jsoup.nodes.Document;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;


import java.io.*;
import java.util.Set;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
public class emailFinderTests {

    @Test
    @DisplayName("Check email.txt is created")
    void testEmailFile()
    {
        String[] param={"http://www.cdm.depaul.edu"};
        EmailFinder emails = new EmailFinder();
        emails.run(param);
        File emailsFile = new File("email.txt");
        assertTrue(emailsFile.exists());
    }

    @Test
    @DisplayName("Check email.txt contents")
    void testEmailFileContents() throws FileNotFoundException {
        String[] param={"http://www.cdm.depaul.edu"};
        EmailFinder emails = new EmailFinder();
        emails.run(param);
        BufferedReader br = new BufferedReader(new FileReader("email.txt"));
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while ((line != null))
            {
                assertTrue(line.contains("@"));
                try {
                    line = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }

    @Test
    @DisplayName("Check for a Document return when url is good in PageFetcher")
    void testPageFetcherGet()
    {
        PageFetcher fetch = new PageFetcher();
        Document test;
        test = fetch.get("http://cdm.depaul.edu");
        assertTrue(test != null);
    }

    @Test
    @DisplayName("PageFetcher should throw exception at invalid url")
    void testInvalidURL()
    {
        PageFetcher fetch = new PageFetcher();
        assertThrows(EmailFinderException.class , () -> {fetch.get("abc.642");});
    }


    @Test
    @DisplayName("Test PageCrawler Constructor.")
    void testPageCrawler()
    {
        StorageService mockStorageService = mock(StorageService.class);
        PageCrawler crawler = new PageCrawler(mockStorageService);
        assertNotNull(crawler);
    }

    @Test
    @DisplayName("Check PageCrawler getEmails for valid emails")
    void testPageCrawlerGetter()
    {
        StorageService mockStorageService = mock(StorageService.class);
        PageCrawler crawler = new PageCrawler(mockStorageService, 30);
        crawler.crawl("http://www.cdm.depaul.edu");
        Set<String> emails = crawler.getEmails();
        for (String email : emails)
        {
            assertTrue(email.contains("@"));
        }
    }

    @ParameterizedTest
    @DisplayName("Test various URLs for valid emails")
    @CsvFileSource(resources = "/urls.csv", numLinesToSkip = 1)
    void testMultipleURL(String url)
    {
        StorageService mockStorageService = mock(StorageService.class);
        PageCrawler crawler = new PageCrawler(mockStorageService, 10);
        crawler.crawl(url);
        Set<String> emails = crawler.getEmails();
        for (String email : emails)
        {
            assertTrue(email.contains("@"));
        }
    }


    @ParameterizedTest
    @DisplayName("Test various URLs for right number of max emails")
    @CsvFileSource(resources = "/urls.csv", numLinesToSkip = 1)
    void testMaxEmail(String url)
    {
        StorageService mockStorageService = mock(StorageService.class);
        PageCrawler crawler = new PageCrawler(mockStorageService, 10);
        crawler.crawl(url);
        Set<String> emails = crawler.getEmails();
        assertEquals(10, emails.size());

    }



}
