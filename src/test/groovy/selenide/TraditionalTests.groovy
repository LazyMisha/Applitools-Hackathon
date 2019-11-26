package selenide

import com.codeborne.selenide.Configuration
import com.tngtech.java.junit.dataprovider.DataProvider
import com.tngtech.java.junit.dataprovider.DataProviderRunner
import com.tngtech.java.junit.dataprovider.UseDataProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static com.codeborne.selenide.Selenide.close
import static com.codeborne.selenide.Selenide.open

@RunWith(DataProviderRunner.class)
class TraditionalTests {

    private CanvasChartPage canvasChartPage = new CanvasChartPage()
    private DataDrivenPage dataDrivenPage = new DataDrivenPage()
    private DynamicContentPage dynamicContentPage = new DynamicContentPage()
    private TableSortPage tableSortPage = new TableSortPage()
    private LoginPage loginPage = new LoginPage()

    final static String app_url_v1 = 'https://demo.applitools.com/hackathon.html'
    final static String app_url_ad_v1 = 'https://demo.applitools.com/hackathon.html?showAd=true'
    final static String app_url_ad_v2 = 'https://demo.applitools.com/hackathonV2.html?showAd=true'

    @Before
    void init(){
        Configuration.startMaximized = true
    }

    @Test
    void 'Canvas chart test'() {
        open app_url_v1
        loginPage.proceedLogin()
        canvasChartPage.clickCompareExpenses()
        canvasChartPage.verifyChart()
        canvasChartPage.clickShowDataForNextYear()
        canvasChartPage.verifyAddedDataInChart(2019)
    }

    @Test
    @UseDataProvider('data_provider_login_form')
    void 'DDT login form test'(String username, String password){
        open app_url_v1
        dataDrivenPage.fillLoginForm(username, password)
        dataDrivenPage.checkRememberMe()
        dataDrivenPage.clickLoginButton()
        dataDrivenPage.checkError()
    }

    @Test
    @UseDataProvider('data_provider_entry_points')
    void 'Dynamic content test'(String url) {
        open url
        loginPage.proceedLogin()
        dynamicContentPage.verifyAds()
    }

    @Test
    void 'Login page test'() {
        open app_url_v1
        loginPage.verify()
    }

    @Test
    void 'Table sort test'() {
        open app_url_v1
        loginPage.proceedLogin()
        tableSortPage.verifyAmountColumnIsUnsorted()
        tableSortPage.clickAmountHeader()
        tableSortPage.verifyAmountColumnIsSorted()
    }

    @DataProvider
    static Object[][] data_provider_login_form() {
        [
                ['', ''],
                ['username', ''],
                ['', 'password'],
                ['username', 'password'],
        ]
    }

    @DataProvider
    static Object[][] data_provider_entry_points() {
        [
                [app_url_ad_v1],
                [app_url_ad_v2]
        ]
    }

    @After
    void tearDown(){
        close()
    }
}
