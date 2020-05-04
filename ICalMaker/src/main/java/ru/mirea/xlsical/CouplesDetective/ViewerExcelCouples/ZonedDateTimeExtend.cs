/*
    Schedule MIREA in calendar.
    Copyright (C) 2020  Mikhail Pavlovich Sidorenko (motherlode.muwa@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

using System.Diagnostics.Contracts;

namespace NodaTime
{
    public static class ZonedDateTimeExtend
    {
        /// <summary>
        /// Returns the result of adding a increment of days to this zoned date and time.
        /// </summary>
        /// <param name="that">Zoned date and time.</param>
        /// <param name="days">The number of hours to add</param>
        /// <returns>A new NodaTime.ZonedDateTime representing the result of the addition.</returns>
        /// <exception cref="NodaTime.SkippedTimeException">Exception thrown to indicate that the specified
        /// local time doesn't exist in a particular time zone due to daylight saving time changes.</exception>
        /// <exception cref="NodaTime.AmbiguousTimeException">Exception thrown to indicate that the specified
        /// local date/time occurs twice in a particular time zone due to daylight saving time changes.</exception>
        [PureAttribute]
        public static ZonedDateTime PlusDays(this ZonedDateTime that, int days)
            => that.Zone.AtStrictly(that.LocalDateTime.Date.PlusDays(days).At(that.LocalDateTime.TimeOfDay));
    }
}