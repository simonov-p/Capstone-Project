package com.simonov.teamfan.objects;

import java.util.List;

/**
 * Created by petr on 04-Jan-16.
 */
public class Game {
    public Team away_team;
    public Team home_team;
    public List<Player> away_stats;
    public List<Player> home_stats;
//    officials // [{u'position': None, u'first_name': u'Pat', u'last_name': u'Fraher'}, {u'position': None, u'first_name': u'Zach', u'last_name': u'Zarba'}, {u'position': None, u'first_name': u'Marat', u'last_name': u'Kogut'}]
    public List<Integer> away_period_scores;
    public List<Integer> home_period_scores;
    public Totals away_totals;
    public Totals home_totals;
//  event_information //{u'status': u'completed', u'start_date_time': u'2016-01-04T19:30:00-05:00', u'attendance': 19874, u'temperature': 0, u'season_type': u'regular', u'site': {u'city': u'Miami', u'state': u'Florida', u'capacity': 19600, u'name': u'AmericanAirlines Arena', u'surface': u'Hardwood'}, u'duration': u'2:38'}



}
