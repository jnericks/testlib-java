package com.jnericks.tests.testlib;

import com.jnericks.testlib.BaseUnitTesterWithSut;

import org.junit.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class SutFactoryDemoTester extends BaseUnitTesterWithSut<SutFactoryDemoTester.ClientLogger> {

    @Test
    public void should_log_client_message() {
        String appId = "an app id";
        String message = "a message";

        ICreateLogger fakeLoggerFactory = fake(ICreateLogger.class);
        ILogMessages fakeLogger = fake(ILogMessages.class);

        given(fakeLoggerFactory.create(appId)).willReturn(fakeLogger);

        ClientLogger sut = new ClientLogger(fakeLoggerFactory);
        sut.logClientMessage(appId, message);

        then(fakeLogger).should(ReceivedOnce).info(message);
    }

    @Test
    public void should_log_client_message_using_sut_factory() {
        String appId = "an app id";
        String message = "a message";

        ILogMessages fakeLogger = fake(ILogMessages.class);

        given(dependency(ICreateLogger.class).create(appId)).willReturn(fakeLogger);

        sut().logClientMessage(appId, message);

        then(fakeLogger).should(ReceivedOnce).info(message);
    }

    public interface ILogMessages {

        void info(String message);
    }

    public interface ICreateLogger {

        ILogMessages create(String appId);
    }

    public class ClientLogger {

        ICreateLogger loggerFactory;

        public ClientLogger(ICreateLogger loggerFactory) {
            this.loggerFactory = loggerFactory;
        }

        public void logClientMessage(String appId, String message) {
            ILogMessages logger = loggerFactory.create(appId);
            logger.info(message);
        }
    }
}
