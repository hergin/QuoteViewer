public class Main {
    public static void main(String[] args) {
        var quoteManager = new QuoteManager();

        var allQuotes = quoteManager.getAllQuotes();

        for (var quote : allQuotes) {
            System.out.println(quote);
        }

        quoteManager.disposeResources();
    }
}
