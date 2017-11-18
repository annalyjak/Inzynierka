package lyjak.anna.inzynierka.viewmodel.report;

/**
 * Created by Anna on 22.10.2017.
 */

public class AdditionalFields {

    private final boolean personalDataAboutEmployee;
    private final boolean purposeOfTravel;
    private final boolean accomodation;
    private final boolean feeding;
    private final boolean publicTransport;
    private final boolean hospital;
    private final boolean other;

    public AdditionalFields(boolean personalDataAboutEmployee,
                            boolean purposeOfTravel,
                            boolean accomodation,
                            boolean feeding,
                            boolean publicTransport,
                            boolean hospital,
                            boolean other) {
        this.personalDataAboutEmployee = personalDataAboutEmployee;
        this.purposeOfTravel = purposeOfTravel;
        this.accomodation = accomodation;
        this.feeding = feeding;
        this.publicTransport = publicTransport;
        this.hospital = hospital;
        this.other = other;
    }

    public AdditionalFields(AdditionalFieldsBuilder builder) {
        this (
                builder.personalDataAboutEmployee,
                builder.purposeOfTravel,
                builder.accomodation,
                builder.feeding,
                builder.publicTransport,
                builder.hospital,
                builder.other
        );
    }

    public static class AdditionalFieldsBuilder {
        private boolean personalDataAboutEmployee = false;
        private boolean purposeOfTravel = false;
        private boolean accomodation = false;
        private boolean feeding = false;
        private boolean publicTransport = false;
        private boolean hospital = false;
        private boolean other = false;

        public AdditionalFieldsBuilder personalDataAboutEmployee(boolean personalDataAboutEmployee){
            this.personalDataAboutEmployee = personalDataAboutEmployee;
            return this;
        }

        public AdditionalFieldsBuilder purposeOfTravel(boolean purposeOfTravel){
            this.purposeOfTravel = purposeOfTravel;
            return this;
        }

        public AdditionalFieldsBuilder accomodation(boolean accomodation){
            this.accomodation = accomodation;
            return this;
        }

        public AdditionalFieldsBuilder feeding(boolean feeding){
            this.feeding = feeding;
            return this;
        }

        public AdditionalFieldsBuilder publicTransport(boolean publicTransport){
            this.publicTransport = publicTransport;
            return this;
        }

        public AdditionalFieldsBuilder hospital(boolean hospital){
            this.hospital = hospital;
            return this;
        }

        public AdditionalFieldsBuilder other(boolean other){
            this.other = other;
            return this;
        }

        public AdditionalFields build() {
            return new AdditionalFields(this);
        }

    }

}
