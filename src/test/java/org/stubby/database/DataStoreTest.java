package org.stubby.database;

import org.eclipse.jetty.http.HttpMethods;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.stubby.handlers.HttpRequestInfo;
import org.stubby.yaml.YamlConsumer;
import org.stubby.yaml.stubs.NotFoundStubResponse;
import org.stubby.yaml.stubs.StubHttpLifecycle;
import org.stubby.yaml.stubs.StubResponse;
import org.stubby.yaml.stubs.UnauthorizedStubResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Alexander Zagniotov
 * @since 6/20/12, 5:27 PM
 */

public class DataStoreTest {

   private static DataStore dataStore;

   @BeforeClass
   public static void beforeClass() throws IOException {
      final URL url = DataStoreTest.class.getResource("/httplifecycles-noheaders.yaml");
      Assert.assertNotNull(url);

      final List<StubHttpLifecycle> stubHttpLifecycles = YamlConsumer.parseYamlFile(url.getFile());
      dataStore = new DataStore();
      dataStore.setStubHttpLifecycles(stubHttpLifecycles);
   }

   @Test
   public void shouldFindHttpLifecycleForGetRequest() throws IOException {

      final String pathInfo = "/invoice/123";

      final HttpServletRequest mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);
      when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethods.GET);
      when(mockHttpServletRequest.getPathInfo()).thenReturn(pathInfo);
      when(mockHttpServletRequest.getQueryString()).thenReturn("");

      final HttpRequestInfo httpRequestInfo = new HttpRequestInfo(mockHttpServletRequest, null);
      final StubResponse stubResponse = dataStore.findStubResponseFor(httpRequestInfo);

      Assert.assertTrue(stubResponse instanceof StubResponse);
      Assert.assertEquals(200, stubResponse.getHttpStatus());
   }

   @Test
   public void shouldFindHttpLifecycleForGetRequestWithAuthorization() throws IOException {

      final String pathInfo = "/invoice/555";

      final HttpServletRequest mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);
      when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethods.GET);
      when(mockHttpServletRequest.getPathInfo()).thenReturn(pathInfo);
      when(mockHttpServletRequest.getQueryString()).thenReturn("");
      when(mockHttpServletRequest.getHeader(HttpRequestInfo.AUTH_HEADER)).thenReturn("Basic Ym9iOnNlY3JldA=="); //bob:secret

      final HttpRequestInfo httpRequestInfo = new HttpRequestInfo(mockHttpServletRequest, null);
      final StubResponse stubResponse = dataStore.findStubResponseFor(httpRequestInfo);

      Assert.assertTrue(stubResponse instanceof StubResponse);
      Assert.assertEquals(200, stubResponse.getHttpStatus());
   }

   @Test
   public void shouldFindHttpLifecycleForGetRequestWithMissingAuthorizationHeader() throws IOException {

      final String pathInfo = "/invoice/555";

      final HttpServletRequest mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);
      when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethods.GET);
      when(mockHttpServletRequest.getPathInfo()).thenReturn(pathInfo);
      when(mockHttpServletRequest.getQueryString()).thenReturn("");


      final HttpRequestInfo httpRequestInfo = new HttpRequestInfo(mockHttpServletRequest, null);
      final StubResponse stubResponse = dataStore.findStubResponseFor(httpRequestInfo);

      Assert.assertTrue(stubResponse instanceof UnauthorizedStubResponse);
      Assert.assertEquals(401, stubResponse.getHttpStatus());
   }

   @Test
   public void shouldFindHttpLifecycleForGetRequestWithAuthorizationWithBadCredentials() throws IOException {

      final String pathInfo = "/invoice/555";

      final HttpServletRequest mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);
      when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethods.GET);
      when(mockHttpServletRequest.getPathInfo()).thenReturn(pathInfo);
      when(mockHttpServletRequest.getQueryString()).thenReturn("");
      when(mockHttpServletRequest.getHeader(HttpRequestInfo.AUTH_HEADER)).thenReturn("Basic 88888nNlY3JldA=="); //bob:secret

      final HttpRequestInfo httpRequestInfo = new HttpRequestInfo(mockHttpServletRequest, null);
      final StubResponse stubResponse = dataStore.findStubResponseFor(httpRequestInfo);

      Assert.assertTrue(stubResponse instanceof UnauthorizedStubResponse);
      Assert.assertEquals(401, stubResponse.getHttpStatus());
   }

   @Test
   public void shouldReturnHttpLifecycleForGetRequestWithDefaultResponse() throws IOException {

      final String pathInfo = "/invoice/125";

      final HttpServletRequest mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);
      when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethods.GET);
      when(mockHttpServletRequest.getPathInfo()).thenReturn(pathInfo);
      when(mockHttpServletRequest.getQueryString()).thenReturn("");

      final HttpRequestInfo httpRequestInfo = new HttpRequestInfo(mockHttpServletRequest, null);
      final StubResponse stubResponse = dataStore.findStubResponseFor(httpRequestInfo);

      Assert.assertTrue(stubResponse instanceof NotFoundStubResponse);
      Assert.assertEquals(404, stubResponse.getHttpStatus());
   }


   @Test
   public void shouldFindHttpLifecycleForPostRequest() throws IOException {

      final String pathInfo = "/invoice/567";
      final String postData = "This is a post data";

      final HttpServletRequest mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);
      when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethods.POST);
      when(mockHttpServletRequest.getPathInfo()).thenReturn(pathInfo);
      when(mockHttpServletRequest.getQueryString()).thenReturn("");

      final HttpRequestInfo httpRequestInfo = new HttpRequestInfo(mockHttpServletRequest, postData);
      final StubResponse stubResponse = dataStore.findStubResponseFor(httpRequestInfo);

      Assert.assertTrue(stubResponse instanceof StubResponse);
      Assert.assertEquals(200, stubResponse.getHttpStatus());
   }


   @Test
   public void shouldReturnHttpLifecycleForPostRequestWithDefaultResponse() throws IOException {

      final String pathInfo = "/invoice/569";
      final String postData = "This is a post data";

      final HttpServletRequest mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);
      when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethods.POST);
      when(mockHttpServletRequest.getPathInfo()).thenReturn(pathInfo);
      when(mockHttpServletRequest.getQueryString()).thenReturn("");

      final HttpRequestInfo httpRequestInfo = new HttpRequestInfo(mockHttpServletRequest, postData);
      final StubResponse stubResponse = dataStore.findStubResponseFor(httpRequestInfo);

      Assert.assertTrue(stubResponse instanceof NotFoundStubResponse);
      Assert.assertEquals(404, stubResponse.getHttpStatus());
   }
}