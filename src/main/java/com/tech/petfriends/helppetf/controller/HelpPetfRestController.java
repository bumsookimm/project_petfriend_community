package com.tech.petfriends.helppetf.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.petfriends.configuration.ApikeyConfig;

@RestController
public class HelpPetfRestController {

	@Autowired
	ApikeyConfig apikeyConfig;

	@GetMapping("/testDiv")
	public ResponseEntity<String> ppap() {
//		인증키+페이지당갯수+페이지+json
		String apikey = apikeyConfig.getOpenDataApikey();
		HttpURLConnection urlConnection = null;
		InputStream stream = null;
		String result = null;
		// abandonmentPublicSrvc 가 두번 들어가야 나오는게 이상하다. 한번만 넣으면 500에러()
		String urlStr = "https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?" +
				"serviceKey=" + apikey + 
				"&_type=" + "json" +
				"&pageNo=" + "1" + 
				"&numOfRows=" + "10";

		try {
			URL url = new URL(urlStr);

			urlConnection = (HttpURLConnection) url.openConnection();
			stream = getNetworkConnection(urlConnection);
			result = readStreamToString(stream);

			if (stream != null)
				stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/* URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환 */
	private InputStream getNetworkConnection(HttpURLConnection urlConnection) throws IOException {
		urlConnection.setConnectTimeout(3000);
		urlConnection.setReadTimeout(3000);
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoInput(true);

		if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException("HTTP error code : " + urlConnection.getResponseCode());
		}

		return urlConnection.getInputStream();
	}


    /* InputStream을 전달받아 문자열로 변환 후 반환 */
    private String readStreamToString(InputStream stream) throws IOException{
        StringBuilder result = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

        String readLine;
        while((readLine = br.readLine()) != null) {
            result.append(readLine + "\n\r");
        }

        br.close();

        return result.toString();
    }
}
