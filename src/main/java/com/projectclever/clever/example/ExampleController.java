package com.projectclever.clever.example;

import com.projectclever.clever.CleverRenderer;
import com.projectclever.clever.CleverHtml;
import com.projectclever.clever.generated.HelloPage;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ExampleController {

    @GetMapping(value = "/")
    public CleverHtml clever(@RequestParam(defaultValue = "world") String param) {
        return CleverRenderer.render(new HelloPage(param));
    }

}
