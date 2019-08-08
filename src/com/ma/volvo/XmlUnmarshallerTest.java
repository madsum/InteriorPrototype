package com.ma.volvo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class XmlUnmarshallerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        // Files.writeString(Path("my", "path"), "My String", StandardOpenOption.CREATE);
        String xml_content = "<Features_res><StartWeek>202017</StartWeek><EndWeek>202035</EndWeek><Pno12>ABCDEEX</Pno12><FeatureList><Feature><Code>10</Code></Feature><Feature><Code>10</Code></Feature></FeatureList><OptionList><Option>1048</Option><Option>50</Option><Option>790</Option></OptionList><CUList><CU><Col>100</Col><Uph>RA0X</Uph><FeatureList><Feature><Code>20</Code></Feature><Feature><Code>30</Code></Feature><Feature><Code>40</Code></Feature><Feature><Code>50</Code></Feature></FeatureList><OptionList><Option><Code>1048</Code><state>optional</state></Option><Option><Code>50</Code><state>available</state></Option><Option><Code>000790</Code><state>optional</state></Option></OptionList></CU></CUList></Features_res>";
        File file = null;
        try {
            file = new File("xml_file.xml");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(xml_content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
        xmlUnmarshaller.UnmarshalXml(file);

    }

}
