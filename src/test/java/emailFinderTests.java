import edu.depaul.email.EmailFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class emailFinderTests {

    @Test
    @DisplayName("Check email.txt for good emails")
    void testEmailFile()
    {
        String[] param={"http://www.cdm.depaul.edu"};
        EmailFinder emails = new EmailFinder();
        emails.run(param);
    }

}
