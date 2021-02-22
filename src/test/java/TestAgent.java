import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.implementation.MethodDelegation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static net.bytebuddy.matcher.ElementMatchers.named;

@RunWith(JUnit4.class)
public class TestAgent {
    @Test
    public void test() throws IllegalAccessException, InstantiationException {
        Class<? extends People> dynamicType = new ByteBuddy()
                .subclass(People.class)
                .name("io.futakotome.ByteBuddyObject")
                .method(named("say"))
                .intercept(MethodDelegation.to(new PeopleImpl()))
                .make()
                .load(TestAgent.class.getClassLoader())
                .getLoaded();
        System.out.println(dynamicType.newInstance().say());
    }

    public interface People {
        String say();
    }

    public class PeopleImpl {
        public String delegateSay() {
            return "hello world";
        }
    }
}
