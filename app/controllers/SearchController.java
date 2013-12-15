package controllers;

import data.FacewallScalaRepo;
import data.ScalaRepository;
import domain.Query;
import facade.SearchFacade;
import facade.modelmapper.PersonDetailsModelMapper;
import facade.modelmapper.SearchResultsModelMapper;
import facade.modelmapper.TeamDetailsModelMapper;
import model.DefaultSearchResultsModel;
import model.PersonDetailsModel;
import model.SearchResultsModel;
import model.TeamDetailsModel;
import play.mvc.Controller;
import play.mvc.Result;
import requestmapper.SearchQueryMapper;

public class SearchController extends Controller {
    private static final ScalaRepository repo = new FacewallScalaRepo();
    private static final SearchQueryMapper searchQueryMapper = new SearchQueryMapper();
    private static final SearchFacade searchFacade = new SearchFacade(repo, new SearchResultsModelMapper(), new PersonDetailsModelMapper(), new TeamDetailsModelMapper());

    public static Result search() {
        return ok(views.html.search.render());
    }

    public static Result searchResults() {
        Result result;
        Query query = searchQueryMapper.map(request());
        SearchResultsModel searchResultsModel = searchFacade.createSearchResultsModel(query);

        //This looks like scala code that has been translated into java. That's fine, but this kind of java code
        //should be avoided if possible. Hopefully we can design it away.
        if(searchResultsModel instanceof DefaultSearchResultsModel) {
            result = ok(views.html.searchresults.render((DefaultSearchResultsModel) searchResultsModel));
        } else if(searchResultsModel instanceof PersonDetailsModel) {
            result = ok(views.html.persondetails.render((PersonDetailsModel) searchResultsModel));
        } else if(searchResultsModel instanceof TeamDetailsModel) {
            result = ok(views.html.teamdetails.render((TeamDetailsModel) searchResultsModel));
        } else {
            //This should be a 4xx rather than a 5xx response
            result = internalServerError("Query not recognized");
        }
        return result;
    }
}
