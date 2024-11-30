package com.lacteos.pruebas;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PedidoTest extends AbstractJavaSamplerClient {

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        String url = "http://<servidor>/api/pedidos"; // Reemplaza con tu URL

        try {
            // Inicia el temporizador de JMeter
            result.sampleStart();

            // Configurar conexión HTTP
            URL endpoint = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Enviar cuerpo de la solicitud
            String payload = "{\"cliente\":\"clienteID\",\"producto\":\"productoID\",\"cantidad\":5}";
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            // Obtener respuesta del servidor
            int responseCode = connection.getResponseCode();
            result.setResponseCode(String.valueOf(responseCode));

            if (responseCode == 200) {
                result.setSuccessful(true);
                result.setResponseMessage("Pedido procesado correctamente.");
            } else {
                result.setSuccessful(false);
                result.setResponseMessage("Error: Código de respuesta " + responseCode);
            }
        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage("Excepción: " + e.getMessage());
        } finally {
            // Detener el temporizador de JMeter
            result.sampleEnd();
        }

        return result;
    }
}
