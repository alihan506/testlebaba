import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.util.Span;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

    public class NameExtractor {
        public static void main(String[] args) throws IOException {
            if (args.length == 0) {
                System.out.println("Lutfen URL'yi girin");
                return;
            }
            String URL = args[0];
            Document document = Jsoup.connect(URL).get();
            Element body = document.body();
            String text = body.text();

            InputStream sentenceInputStream = new FileInputStream("src/main/resources/en-sent.bin");
            SentenceModel Model = new SentenceModel(sentenceInputStream);
            SentenceDetectorME Detector = new SentenceDetectorME(Model);

            InputStream tokenizerInput = new FileInputStream("src/main/resources/en-token.bin");
            TokenizerModel TokenizerM = new TokenizerModel(tokenizerInput);
            Tokenizer tokenizer = new TokenizerME(TokenizerM);

            InputStream nameInput = new FileInputStream("src/main/resources/en-ner-person.bin");
            TokenNameFinderModel nameModel = new TokenNameFinderModel(nameInput);
            NameFinderME nameFinder = new NameFinderME(nameModel);


            String[] cumle = Detector.sentDetect(text);

            for (String sentence : cumle) {
                String[] tokens = tokenizer.tokenize(sentence);
                Span[] nameSpans = nameFinder.find(tokens);
                String[] names = Span.spansToStrings(nameSpans, tokens);
                for (String name : names) {
                    System.out.println(name);
                }
            }

            sentenceInputStream.close();
            tokenizerInput.close();
            nameInput.close();
        }
    }

