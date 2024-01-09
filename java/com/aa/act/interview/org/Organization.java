package com.aa.act.interview.org;

import java.util.Optional;

public abstract class Organization {

    private Position root;
    private int id = 1;

    public Organization() {
        root = createOrganization();
    }

    protected abstract Position createOrganization();

    /**
     * hire the given person as an employee in the position that has that title
     *
     * @param person
     * @param title
     * @return the newly filled position or empty if no position has that title
     */
    public Optional<Position> hire(Name person, String title) {
        return check(root, person, title);
    }


    private Optional<Position> check(Position checkPosition, Name name, String title) {
        if(checkPosition.getTitle().equals(title)){
            return addEmp(checkPosition, name);
        }
        // check title if it matches position title then set employee
        for (Position p : checkPosition.getDirectReports()) {
            if (p.getTitle().equals(title)) {
                return addEmp(p, name);
            } else if (!p.getDirectReports().isEmpty()) {
                //if title doesn't match, use the new position, and go through the loop again, until a position is found or the collection is exhausted.
                Optional<Position> position = check(p, name, title);
                if (position.isPresent()) {
                    return position;
                }
            }
        }
        //if no title match return empty
        return Optional.empty();
    }

    private Optional<Position> addEmp(Position position, Name name) {
        // check if position is filled already
        if (!position.isFilled()) {
            //if not filled set the employee in the position
            position.setEmployee(Optional.of(new Employee(id, name)));
            id++;
            return Optional.of(position);
        } else {
            throw new IllegalArgumentException("Position is filled");
        }
    }

    @Override
    public String toString() {
        return printOrganization(root, "");
    }

    private String printOrganization(Position pos, String prefix) {
        StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
        for (Position p : pos.getDirectReports()) {
            sb.append(printOrganization(p, prefix + "  "));
        }
        return sb.toString();
    }
}

