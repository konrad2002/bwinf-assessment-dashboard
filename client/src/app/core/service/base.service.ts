export abstract class BaseService {
  private readonly serviceName: string;
  protected readonly baseUrl: string;

  protected constructor(serviceName: string, baseUrl: string) {
    this.serviceName = serviceName;
    this.baseUrl = baseUrl;
  }

  log(msg: string, type: "info" | "warning" | "error" | "success" = "info") {
    console.log("[" + type.toUpperCase() +  " | " + this.serviceName +  "]: > " + msg);
  }
}
