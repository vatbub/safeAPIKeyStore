package com.github.vatbub.safeAPIKeyStore.client;

import com.github.vatbub.safeAPIKeyStore.common.BadRequestExceptionInternalImpl;
import com.github.vatbub.safeAPIKeyStore.common.InternalServerExceptionInternalImpl;
import com.github.vatbub.safeAPIKeyStore.common.MultipleRequestsWithSameRSAKeyExceptionInternalImpl;
import com.github.vatbub.safeAPIKeyStore.server.Server;

import java.io.File;
import java.io.IOException;

public class FakeServer extends Server {
    private FakeResponse fakeResponse;

    /**
     * Launches a server on the specified port with the specified api keys.
     *
     * @param port            The port to run the server on
     * @param apiKeysFileName The absolute or relative file path to the properties-file that contains the api keys to serve
     * @throws IOException If the server fails to launch for any reason.
     */
    public FakeServer(int port, String apiKeysFileName, FakeResponse fakeResponse) throws IOException {
        super(port, apiKeysFileName);
        setFakeResponse(fakeResponse);
    }

    /**
     * Launches a server on the specified port with the specified api keys.
     *
     * @param port        The port to run the server on
     * @param apiKeysFile The properties-file that contains the api keys to serve
     * @throws IOException If the server fails to launch for any reason.
     */
    public FakeServer(int port, File apiKeysFile, FakeResponse fakeResponse) throws IOException {
        super(port, apiKeysFile);
        setFakeResponse(fakeResponse);
    }

    @Override
    public Object createResponse(Object receivedObject) {
        switch (getFakeResponse()) {
            case TRUE_ANSWER:
                return super.createResponse(receivedObject);
            case TIMEOUT:
                return null;
            case BAD_REQUEST_EXCEPTION:
                return new BadRequestExceptionInternalImpl("Fake bad request exception");
            case INTERNAL_SERVER_EXCEPTION:
                return new InternalServerExceptionInternalImpl("Fake internal server exception");
            case MULTIPLE_REQUESTS_WITH_SAME_RSA_KEY_EXCEPTION:
                return new MultipleRequestsWithSameRSAKeyExceptionInternalImpl("Fake MRWSRSAK exception");
            case ILLEGAL_OBJECT:
                return "Illegal object";
            default:
                // will never happen, but the compiler wants it
                return super.createResponse(receivedObject);
        }
    }

    public FakeResponse getFakeResponse() {
        return fakeResponse;
    }

    public void setFakeResponse(FakeResponse fakeResponse) {
        this.fakeResponse = fakeResponse;
    }

    public enum FakeResponse {
        TRUE_ANSWER, TIMEOUT, BAD_REQUEST_EXCEPTION, INTERNAL_SERVER_EXCEPTION, MULTIPLE_REQUESTS_WITH_SAME_RSA_KEY_EXCEPTION, ILLEGAL_OBJECT
    }
}
