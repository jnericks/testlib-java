package com.jnericks.tests.testlib;

import com.jnericks.testlib.BaseUnitTesterWithSut;
import org.junit.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.powermock.api.mockito.PowerMockito.mock;

public class SutFactoryDemoTester extends BaseUnitTesterWithSut<SutFactoryDemoTester.ClientLogger>
{
    @Test
    public void should_log_client_message()
    {
        String appId = "an app id";
        String message = "a message";

        ICreateLogger fakeLoggerFactory = mock(ICreateLogger.class);
        ILogMessages fakeLogger = mock(ILogMessages.class);

        given(fakeLoggerFactory.create(appId)).willReturn(fakeLogger);

        ClientLogger sut = new ClientLogger(fakeLoggerFactory);
        sut.logClientMessage(appId, message);

        then(fakeLogger).should(ReceivedOnce).info(message);
    }

    @Test
    public void should_log_client_message_using_sut_factory()
    {
        String appId = "an app id";
        String message = "a message";

        ILogMessages fakeLogger = mock(ILogMessages.class);

        given(SutFactory.dependency(ICreateLogger.class).create(appId)).willReturn(fakeLogger);

        SutFactory.sut().logClientMessage(appId, message);

        then(fakeLogger).should(ReceivedOnce).info(message);
    }

    public interface ILogMessages
    {
        void info(String message);
    }

    public interface ICreateLogger
    {
        ILogMessages create(String appId);
    }

    public class ClientLogger
    {
        private ICreateLogger _loggerFactory;

        public ClientLogger(ICreateLogger loggerFactory)
        {
            _loggerFactory = loggerFactory;
        }

        public void logClientMessage(String appId, String message)
        {
            ILogMessages logger = _loggerFactory.create(appId);
            logger.info(message);
        }
    }
}
