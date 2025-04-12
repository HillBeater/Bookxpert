package com.hillbeater.bookxpert.instance

import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

object AccessToken {

    private val firbaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"

    fun getAccessToken() : String?{
        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"authentications-ed72e\",\n" +
                    "  \"private_key_id\": \"8c4362ab63be5b685a25afda1d9aac699cdd6ac6\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDFtvUYPrwQauCa\\n4nmAD7XReIqrpW9XIEAwW71MDHhmpzXrne0Zhywx43CBeDnH05AU/kYSMmQhjYMs\\nMrZtl+AGRtLhGwp6AffUMF+NraAVKD6h6cMAyaHQdidZVxdG7lZHLQzxadE5vi4j\\nVruNVIu8zZbVMs1SqQ4nrRA6Fb9oWtlNlq8oe6s3kJxli3BP5/YyXHxVmlOwC4ya\\n/MT+AprhaypuLn6OTCEVGUYp1L6AphVqmhB6iTzB+odK7vpSDNBOvuHY7Xm8bb2O\\nTUglWk0cciSibbdqGSQ/Yu306e3+b413thJswAlDmXykrfmDw1k9tctAxmdALdH/\\n+7JybXC5AgMBAAECggEAB5Kxsh9LCEzzs/MLSNixM+gMVbVFPb7WPpPw/P34SaRL\\neC4pJAJtsq59w3PFsM39dZRkvjMwaymWteVuX+XClrL1RlqJDPHQ2o+MxWo6WL0d\\nIwoHZlsSx9+Ss8UWdujPpLPIzGFremz6qu9aSThNDusIBTwIKcugZ2wlVDg7h+Vr\\nrARgXVbjvuxZo1POlU0SibyiSwEJeHI6Reo7P0deE0G7iA2V5qzCE1XhUv9WBkrI\\nrcs6gMai5IqFXV+NyQiUGNL9Tvy8LyO84NaFj9hft0ZNiCKLPOBtlPyruqHYSICC\\nodtpJr1UmuUsAYW16KtFbsuUNbW9v6F9u7drg1o9IQKBgQDutyIlV0XfOCBAShcP\\nCbjGKrEgf6z+2zzfiow2oszudpMWOdBibWOwlfnaAxJDdnyK3TYIZtnGvxL957FR\\nW2WSYsdc62OnnuSoV0M2OOO5jXF4P3oZTEC3cdr4ZdxGLLBB2NXLyoLL98lfM/uR\\nFLAiBAzSgAevEBQbcKsn9SK76QKBgQDUB9QiB1Yq3Cs8PswEW48kurtkcGVd279U\\ntTdneYMXtxHSf8Vym62LfJpr/ESU7hX2sjc0qwMlOT58XsmWphn70mleVIKWi5T2\\nQXoohreidVOnxze0Gg4faENHI46RuvmxGyl/f7HYl24EvaVI6IRhNaFsrUV+JdOq\\nWhB/mb6cUQKBgQDYfxSjwwPmjzOIuukwxUNjziI4dDLELLeHkVLa1kl/1+BMHwDj\\n04u/AjXISFLPBm2rTzaK5iSKQI6L4g6J15dF7PocRotp4QxFtGaLjYrSpPoM8Zzd\\nxq9G8CaEc3UT28G65ln36mE3dTGXe0CjcpzPLNcoLd9KIQ3rWQKRSZxHyQKBgD6g\\n6Vn7zMnwKp0q91w6Jzf0KDHsuSRWKle9B1Z7H+u7WXpR32KQ9VwEPCC1bRSIltg9\\nn+zgrpcqJ3jZDrZ+7aHibtk8IS6SoDd0875QvKEyArnGMH/0SkOgqm6y5APLtMFJ\\ngqJ0d3XAlPC1i8LTW3bTPpE5RROT3MeuvrzSDulhAoGBAMbsgJrAhY6G5ZQ8P/ko\\nXqcxGz2JMJenHjo/mTSfdo6U6uwQwrfN+ZcBwK+Dh27YWd5Tee89FXQC9OY+6IoQ\\nuBSgf+LeSgSkE1OscLwlrS3Td4fi1IeNDGyKiFbUwxzsOrNtmkgUVbIqTKtVOCC/\\nyS7ILHH65sExXlBUj4+SzMf8\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-65g9s@authentications-ed72e.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"107688297894791462064\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-65g9s%40authentications-ed72e.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n"

            val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))

            val googleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(firbaseMessagingScope))

            googleCredentials.refresh()

            return googleCredentials.accessToken.tokenValue

        }catch (ex: Exception){
            return null
        }
    }
}