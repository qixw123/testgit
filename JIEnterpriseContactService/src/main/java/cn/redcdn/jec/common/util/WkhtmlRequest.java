package cn.redcdn.jec.common.util;

/**
 * @author jillukowicz
 */
public class WkhtmlRequest {

  private String in;
  private String out;

  public String getIn() {
    return in;
  }

  public void setIn(String in) {
    this.in = in;
  }

  public String getOut() {
    return out;
  }

  public void setOut(String out) {
    this.out = out;
  }

  @Override
  public String toString() {
    return "WkhtmlRequest{" +
        "in='" + in + '\'' +
        ", out='" + out + '\'' +
        '}';
  }
}
