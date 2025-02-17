package Repository.DataBase;

import Repository.Pagination.PagingRepository;
import org.example.lab6.Project.Application.Domain.*;
import java.util.*;
import java.sql.*;

import org.example.lab6.Project.Application.Domain.Validators.UserValidator;
import Repository.Pagination.*;

public class UserDBRepository implements PagingRepository<Long,User> {


    UserValidator userValidator;

    public UserDBRepository(UserValidator userValidator) {
        this.userValidator = userValidator;
    }


    @Override
    public Optional<User> findOne(Long aLong) {
        String query = "select * from users WHERE \"id\" = ?";
        User user = null;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "dragos");
             PreparedStatement statement = connection.prepareStatement(query);) {

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password=resultSet.getString("password");
                String image=resultSet.getString("image");
                user = new User(aLong, firstName, lastName,password,image);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Iterable<User> findAll() {
        HashMap<Long, User> users = new HashMap<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "dragos");
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("first_name");
                String prenume = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                String imageUrl = resultSet.getString("image");
                User user = new User(id, nume, prenume,password,imageUrl);

                users.put(user.getId(), user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users.values();
    }

    @Override
    public Optional<User> save(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User can't be null!");
        }
        if (entity.getProfileImageUrl() != null) {

            String query = "UPDATE users SET \"image\" = ? WHERE \"id\" = ?";
            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "dragos");
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, entity.getProfileImageUrl());
                statement.setLong(2, entity.getId());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {

            String query = "INSERT INTO users(\"id\", \"first_name\", \"last_name\", \"password\") VALUES (?,?,?,?)";

            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "dragos");
                 PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setLong(1, entity.getId());
                statement.setString(2, entity.getFirstName());
                statement.setString(3, entity.getLastName());
                statement.setString(4, entity.getPassword());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return Optional.of(entity);
    }


    @Override
    public Optional<User> delete(Long aLong) {


        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "dragos");
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE \"id\" = ?");) {
            statement.setLong(1, aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        User userToDelete = null;
        for (User user : findAll()) {
            if (Objects.equals(user.getId(), aLong)) {
                userToDelete = user;
            }
        }
        return Optional.ofNullable(userToDelete);
    }

    @Override
    public Optional<User> update(User entity) {
        return Optional.empty();
    }

    @Override
    public Page<User> findall(Pageable pageable) {
        List<User> userList = new ArrayList<>();
        try(Connection connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "dragos");
            PreparedStatement pagePreparedStatement=connection.prepareStatement("SELECT * FROM users " +
                    "LIMIT ? OFFSET ?");

            PreparedStatement countPreparedStatement = connection.prepareStatement
                    ("SELECT COUNT(*) AS count FROM users ");

        ) {
            pagePreparedStatement.setInt(1, pageable.getPageSize());
            pagePreparedStatement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet pageResultSet = pagePreparedStatement.executeQuery();
                 ResultSet countResultSet = countPreparedStatement.executeQuery(); ) {
                while (pageResultSet.next()) {
                    Long id = pageResultSet.getLong("id");
                    String firstName = pageResultSet.getString("first_name");
                    String lastName = pageResultSet.getString("last_name");
                    String password=pageResultSet.getString("password");
                    String image=pageResultSet.getString("image");
                    User user = new User(id,firstName, lastName,password,image);
                    user.setId(id);
                    userList.add(user);
                }
                int totalCount = 0;
                if(countResultSet.next()) {
                    totalCount = countResultSet.getInt("count");
                }

                return new Page<>(userList, totalCount);

            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}

